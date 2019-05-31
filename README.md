# TwoWayCommunicationSystem
This will give you a general description of how to use our product.

1. Open the project in NetBeans or Eclipse, in future we plan to have an direct application which will reduce the workload of opening the project in any IDE.
2. After you open the project in NetBeans navigate to CommanLanMonitoringServer this is the main file that you will be running after you start the server.
3. In the project folder you will have an recognize.py Python file which is an server file that acts as an server and connects with your project running on NetBeans, run this file using the python compiler.
4. Run CommanLanMonitoringServer.java file in NetBeans which will act as an client.
5. After connection is successful it will show the IP and the port number which will be further used to connect with our Android Application.
6. An image of all ASL gestures is provided in the package you can use that as reference while executing the project.
7. Make sure that you have a lighter background and darker foreground as we are working with greyscale images and it gives maximum accuracy in this case(white backgound is preffered).
8. Press 'Start' and that will start the camera of the Laptop.
9. Make sure to place your hand in the defined box and the converted sign's output will be displayed on the screen along with the text.
10. To perform Text/Speech to ASL shift to the TextToASL tab.
11. Here you can either type text and it will be converted into Sign Language or you can record speech and then send it to the machine using an Android Application.
12. Install the provided application on your Android deivce and give necessary permissions.
13. When you open the application it will ask for IP and port number which we saw was displayed on the console of NetBeans IDE when we executed our project.
14. Make sure the Android Device and your machine are connected to the same network for this to work, put the IP address and port number and device and machine will be connected.
15. After the connnection is successful press volume down button to record speech and it will be directly sent to machine.
16. The output will be given in sign language after converting the speech into text into sign.
