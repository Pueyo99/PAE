from flask import Flask
from flask import jsonify
from PIL import Image
from flask import request
import base64
from io import BytesIO
import datetime


SQR_M_PERSON = 4.0

app = Flask(__name__)

@app.route('/<float:room_dim>', methods = ['GET'])
def max_cap(room_dim):

    max_capacity = int(room_dim / SQR_M_PERSON)
    return jsonify(

        {'max_cap': max_capacity}
        
        )


@app.route('/image', methods = ['POST'])
def read_image():
	json = request.json
	content = base64.b64decode(json['image'])
	filename = json['filename']
	rotation = json['rotation']
	print(rotation)
	image = Image.open(BytesIO(content)).rotate((rotation*90 -90), expand=True)
	image.save("images/"+filename+".jpeg")
	return jsonify({'prueba':'Imagen guardada'})


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')