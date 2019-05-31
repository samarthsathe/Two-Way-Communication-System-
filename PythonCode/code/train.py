import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import keras
import cv2
from keras.callbacks import ModelCheckpoint
import numpy as np
from IPython.display import Image
from sklearn.preprocessing import LabelBinarizer
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense, Conv2D, MaxPooling2D, Flatten, Dropout
from sklearn.metrics import accuracy_score,confusion_matrix, accuracy_score, f1_score
from keras.models import model_from_yaml
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

#Image("../input/amer_sign2.png")

train = pd.read_csv('../input/sign_mnist_train.csv')
test = pd.read_csv('../input/sign_mnist_test.csv')

train.head()
train.shape

#Image("../input/american_sign_language.PNG")

labels = train['label'].values
unique_val = np.array(labels)
np.unique(unique_val)

plt.figure(figsize = (18,8))
sns.countplot(x =labels)

train.drop('label', axis = 1, inplace = True)
#Re shaping the images
images = train.values
images = np.array([np.reshape(i, (28, 28)) for i in images])
images = np.array([i.flatten() for i in images])

label_binrizer = LabelBinarizer()
labels = label_binrizer.fit_transform(labels)
labels
cv2.imshow("d",images[0].reshape(28,28))
cv2.imshow("d1",images[1].reshape(28,28))
cv2.imshow("d2",images[2].reshape(28,28))
#view Img
plt.imshow(images[0].reshape(28,28))
#Spliting the dataset into train(70%) and test(30%)
x_train, x_test, y_train, y_test = train_test_split(images, labels, test_size = 0.3, random_state = 101)
#Creating the batch size to 128 and using 50 epochs
batch_size = 128
num_classes = 24
epochs = 100
#epochs = 1
#Normalizing the training and test data
x_train = x_train / 255
x_test = x_test / 255
x_train = x_train.reshape(x_train.shape[0], 28, 28, 1)
x_test = x_test.reshape(x_test.shape[0], 28, 28, 1)
#Visualizing the image after normalizing
#plt.imshow(x_train[0].reshape(28,28))
#CNN Model
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
              
filepath="weights-improvement-{epoch:02d}-{val_acc:.2f}.hdf5"
checkpoint = ModelCheckpoint(filepath, monitor='val_acc', verbose=1, save_best_only=True, mode='max')
callbacks_list = [checkpoint]
history = model.fit(x_train, y_train, validation_data = (x_test, y_test), epochs=epochs, batch_size=batch_size,callbacks=callbacks_list)

plt.plot(history.history['acc'])
plt.plot(history.history['val_acc'])
plt.title("Accuracy")
plt.xlabel('epoch')
plt.ylabel('accuracy')
plt.legend(['train','test'])
#plt.show()
# validate with the test data
test_labels = test['label']
test.drop('label', axis = 1, inplace = True)
test_images = test.values
test_images = np.array([np.reshape(i, (28, 28)) for i in test_images])
test_images = np.array([i.flatten() for i in test_images])
test_labels = label_binrizer.fit_transform(test_labels)
test_images = test_images.reshape(test_images.shape[0], 28, 28, 1)
test_images.shape
#Predecting with test images
y_pred = model.predict(test_images)
"""
im = cv2.imread('test.png')
im=cv2.resize(im,(28,28))
gray = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
print(gray.shape)
reshaped=np.reshape(gray,(1,28,28,1))

y_pred = model.predict(reshaped)
classDet=np.argmax(y_pred, -1)
print(y_pred)
print(classes[int(classDet)])
"""

model_yaml = model.to_yaml()
with open("model.yaml", "w") as yaml_file:
    yaml_file.write(model_yaml)
# serialize weights to HDF5
model.save_weights("model.h5")
print("Saved model to disk")
print("Saved model to disk")


A=accuracy_score(test_labels, y_pred.round())
print(A)

CF=confusion_matrix(test_labels.argmax(axis=1), y_pred.round().argmax(axis=1))
print(CF)
A=test_labels.argmax(axis=1)
B=y_pred.round().argmax(axis=1)
print(A)
print(B)
