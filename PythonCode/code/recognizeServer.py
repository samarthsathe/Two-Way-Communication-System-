import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import keras
import socket
import sys
import cv2
from IPython.display import Image
from sklearn.preprocessing import LabelBinarizer
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense, Conv2D, MaxPooling2D, Flatten, Dropout
from sklearn.metrics import accuracy_score
from keras.models import model_from_yaml
import os
# for graphic card start
import tensorflow as tf
from keras.backend.tensorflow_backend import set_session
classes=['A','B','C','D','E','F','G','H','I','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y']

config = tf.ConfigProto(
    gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.8)
    # device_count = {'GPU': 1}
)
config.gpu_options.allow_growth = True
session = tf.Session(config=config)
set_session(session)

# for graphic card end


classes=['A','B','C','D','E','F','G','H','I','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y']

batch_size = 128
num_classes = 24
epochs = 200
model = Sequential()
model.add(Conv2D(64, kernel_size=(3,3), activation = 'relu', input_shape=(28, 28 ,1) ))
model.add(MaxPooling2D(pool_size = (2, 2)))

model.add(Conv2D(64, kernel_size = (3, 3), activation = 'relu'))
model.add(MaxPooling2D(pool_size = (2, 2)))

model.add(Conv2D(64, kernel_size = (3, 3), activation = 'relu'))
model.add(MaxPooling2D(pool_size = (2, 2)))

model.add(Flatten())
model.add(Dense(128, activation = 'relu'))
model.add(Dropout(0.20))
model.add(Dense(num_classes, activation = 'softmax'))
model.compile(loss = keras.losses.categorical_crossentropy, optimizer=keras.optimizers.Adam(),
              metrics=['accuracy'])
model.load_weights("model.h5")
        
HOST = ''  # Symbolic name, meaning all available interfaces
PORT = 7813  # Arbitrary non-privileged port

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

# Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
    sys.exit()

print('Socket bind complete')

# Start listening on socket
s.listen(10)
print('Socket now listening')


while 1:
    # wait to accept a connection - blocking call
    conn, addr = s.accept()
    print('Connected with ' + addr[0] + ':' + str(addr[1]))
    try:
        data = conn.recv(1024).decode()
        print(data)
        image_path = data.strip()
        exists = os.path.isfile(image_path)
        if (exists):
            filename = image_path
            im = cv2.imread(filename)
            im=cv2.resize(im,(28,28))
            gray = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
            
            reshaped=np.reshape(gray,(1,28,28,1))
            y_pred = model.predict(reshaped)
            print(y_pred)
            classDet=np.argmax(y_pred, -1)
            print(classDet)
            r = ",".join(str(x) for x in classes)
            r = str(classDet[0])+'#'+r+"#"+classes[int(classDet)]
            print(r)
            conn.send(r.encode())
    except:
        type, value, traceback = sys.exc_info()
        print('Error opening %s: %s' % (value.filename, value.strerror))
    conn.close()
s.close()