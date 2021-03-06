from flask import Flask, jsonify, request
from PIL import Image
import base64
from io import BytesIO
import datetime
import os
import time
import json
from pre_process_image import resize_image

app = Flask(__name__)

@app.route('/<float:room_dim>', methods = ['GET'])
def max_cap(room_dim):
    SQR_M_PERSON = 4.0
    max_capacity = int(room_dim / SQR_M_PERSON)
    return jsonify(

        {'max_cap': max_capacity}

        )


@app.route('/window', methods = ['POST'])
def compute_window():
	r = request.json
	content = base64.b64decode(r['image'])
	filename = r['filename']
	rotation = r['rotation']
	print(rotation)
	image = Image.open(BytesIO(content)).rotate((rotation*90 -90), expand=True)
	image = resize_image(image)

	image.save("test_images/"+filename+".jpg")

	# Run test.py
	os.system("python3 -u test.py --imgs test_images/" +filename+ ".jpg --gpu 0 --cfg config/ade20k-resnet50dilated-ppm_deepsup.yaml")
	
	while not os.path.exists("json_results/"+filename+".json"):
		time.sleep(5)
	json_file = json.loads(open("json_results/"+filename+".json").read())
	return jsonify(json_file)

@app.route('/mask', methods = ['POST'])
def compute_mask():
	r = request.json
	content = base64.b64decode(r['image'])
	filename = r['filename']
	rotation = r['rotation']
	print(rotation)
	image = Image.open(BytesIO(content)).rotate((rotation*90 - 90), expand=True)
	image = resize_image(image)

	image.save("mask_detection/images/"+filename+".jpg")

	#Run demo.py
	os.system("python3 mask_detection/demo.py -n prn -i mask_detection/images/"+filename+".jpg")
	
	while not os.path.exists("mask_detection/json_results/"+filename+".json"):
		time.sleep(5)
	json_file = json.loads(open("mask_detection/json_results/"+filename+".json").read())
	
	return jsonify(json_file)

@app.route('/people', methods = ['POST'])
def count_people():
	r = request.json
	content = base64.b64decode(r['image'])
	filename = r['filename']
	rotation = r['rotation']
	print(rotation)
	image = Image.open(BytesIO(content)).rotate((rotation*90 - 90), expand=True)
	image = resize_image(image)

	image.save("object_counter/images/"+filename+".jpg")

	#Run images.py
	os.system("python3 object_counter/images.py --images object_counter/images/"+filename+".jpg")

	while not os.path.exists("object_counter/json_results/"+filename+".json"):
		time.sleep(5)
		print("Esperando al fichero: "+filename+".json")
	json_file = json.loads(open("object_counter/json_results/"+filename+".json").read())

	return jsonify(json_file)
     

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8999)
