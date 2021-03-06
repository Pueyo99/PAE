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


@app.route('/image', methods = ['POST'])
def read_image():
	r = request.json
	content = base64.b64decode(r['image'])
	filename = r['filename']
	rotation = r['rotation']
	print(rotation)
	image = Image.open(BytesIO(content)).rotate((rotation*90 -90), expand=True)
	image = resize_image(image)

	image.save("images/"+filename+".jpg")

	# Run test.py
	os.system("python3 demo.py -n prn -i images/"+filename+".jpg")


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8999)
