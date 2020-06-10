// use libraries
#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "SOIL2/SOIL2.h"

using namespace std;

// Macros for window information
#define WINDOW_TITLE "Jamie Horner"

#ifndef GLSL
#define GLSL(Version, Source) "#version " #Version "\n" #Source
#endif

/* Variable declarations for shader, window size initialization, buffer and array objects*/
GLint shaderProgram, WindowWidth = 800, WindowHeight = 600, currentMouseButton;
GLuint VBO, VAO, EBO, texture;

// cube position and scale
glm::vec3 bookshelfPosition(0.0f, 0.0f, 0.0f);
glm::vec3 bookshelfScale(2.0f);

// cube and light color
glm::vec3 objectColor(0.5f, 0.5f, 0.70f);
glm::vec3 lightColor(1.0f, 1.0f, 1.0f);

// light position and scale
glm::vec3 lightPosition(1.0f, -1.0f, 3.0f);
glm::vec3 lightScale(2.0f);

GLfloat cameraSpeed = 0.0005f; //Movement speed per frame
GLchar currentKey; //Will store key pressed

GLfloat lastMouseX = 400, lastMouseY = 300;
GLfloat mouseXOffset, mouseYOffset, yaw = 0.0f, pitch = 0.0f;
GLfloat sensitivity = 0.005f;
bool mouseDetected = true, isMode3d = true;

//global vector declarations
glm::vec3 cameraPosition = glm::vec3(5.0f, 0.0f, 0.0f); // Initial camera position. Placed units in Z
glm::vec3 CameraUpY = glm::vec3(0.0f, 1.0f, 0.0f); //Temporary y unit vector
glm::vec3 CameraForwardZ = glm::vec3(0.0f, 0.0f, 1.0f); //Temporary z unit vector
glm::vec3 front = glm::vec3(0.0f, 0.0f, 20.0f);

//Function Declarations
void UResizeWindow(int, int);
void URenderGraphics(void);
void UCreateShader(void);
void UCreateBuffers(void);
void UGenerateTexture(void);

void UKeyboard(unsigned char key, int x, int y);
void UKeyReleased(unsigned char key, int x, int y);
void UMouseClick(int button, int state, int x, int y);
void UMouseMove(int x, int y);

// Base shader for vertex positions
const GLchar * vertexShaderSource = GLSL(330,

		// Vertex data from Vertex Attrib Pointer 0
        layout(location = 0) in vec3 position;

		// Normal lighting data from Vertex Attrib Pointer 1
        layout(location = 1) in vec3 normal;

        // Texture data from Vertex Attrib Pointer 2
        layout(location = 2) in vec2 textureCoordinate;

		//Variables to transfer texture and lighting data to the fragment shader
        out vec3 Normal;
        out vec3 FragmentPos;
        out vec2 mobileTextureCoordinate;

		//Global variables for the transform matrices
        uniform mat4 model;
        uniform mat4 view;
        uniform mat4 projection;

	void main(){
			// transforms vertices to clip coordinates
			gl_Position =  projection * view * model * vec4(position, 1.0f);

			// gets fragment / pixel position in the world space only
			FragmentPos = vec3(model * vec4(position, 1.0f));

			//get normal vectors in world space only
			Normal = mat3(transpose(inverse(model))) * normal;

			//calculate texture layout from coordinates
			mobileTextureCoordinate = vec2(textureCoordinate.x, 1.0f - textureCoordinate.y);
		}
);

// Lighting and texture shader
const GLchar * fragmentShaderSource = GLSL(330,

        in vec3 Normal; // for incoming normals
        in vec3 FragmentPos;  // for incoming fragment position
        in vec2 mobileTextureCoordinate; // for incoming texture layout

        out vec4 BookshelfShading; // outgoing bookshelf color information

        uniform vec3 objectColor; // Global base object color
        uniform vec3 lightColor; // Global light color
        uniform vec3 lightPos; // Global light source position
        uniform vec3 viewPosition; // Global camera position
		uniform sampler2D uTexture; // Global texture object

	void main(){

		// calculate ambient lighting
		float ambientStrength = 0.1f;
		vec3 ambient = ambientStrength * lightColor;

		// Calculate diffuse lighting
		vec3 norm = normalize(Normal);
		vec3 lightDirection = normalize(lightPos - FragmentPos);
		float impact = max(dot(norm, lightDirection), 0.0);
		vec3 diffuse = impact * lightColor;

		// calculate specular lighting
		float specularIntensity = 2.0f;
		float highlightSize = 10.0f;
		vec3 viewDir = normalize(viewPosition - FragmentPos);
		vec3 reflectDir = reflect(-lightDirection, norm);

		// calculate specular component
		float specularComponent = pow(max(dot(viewDir, reflectDir), 0.0), highlightSize);
		vec3 specular = specularIntensity * specularComponent * lightColor;

		// Combine all light, color, and texture data
		vec3 phong = (ambient + diffuse + specular) * objectColor;
		BookshelfShading = vec4(phong, 1.0f) * texture(uTexture, mobileTextureCoordinate);//send lighting results to GPU
	}
);

// Initialize and loop through OpenGL functions
int main (int argc, char* argv[]) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowSize(WindowWidth, WindowHeight);
	glutCreateWindow(WINDOW_TITLE);

	glutReshapeFunc(UResizeWindow);

	//Initialize glew and check platform openGL compatibility
	glewExperimental = GL_TRUE;
	if (glewInit() != GLEW_OK){
		std::cout << "Failed to initialize GLEW" << std::endl;
		return -1;
	}

	UCreateShader();

	UCreateBuffers();

	UGenerateTexture();

	glUseProgram(shaderProgram);

	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	glutDisplayFunc(URenderGraphics);

	// Detect user input and run callback function
	glutMotionFunc(UMouseMove);
	glutMouseFunc(UMouseClick);
	glutKeyboardFunc(UKeyboard);
	glutKeyboardUpFunc(UKeyReleased);

	glutMainLoop();

	// Destroys Buffer objects once used
	glDeleteVertexArrays(1, &VAO);
	glDeleteBuffers(1, &VBO);

	return 0;
}

// triggered on window resize
void UResizeWindow (int w, int h) {
	WindowWidth = w;
	WindowHeight = h;
	glViewport(0, 0, WindowWidth, WindowHeight);
}

// does calculations for object and camera each frame
void URenderGraphics (void) {

	glEnable(GL_DEPTH_TEST);

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	GLint modelLoc, viewLoc, projLoc, objectColorLoc, lightColorLoc, lightPositionLoc, viewPositionLoc;

	glUseProgram(shaderProgram);
	glBindVertexArray(VAO);

	CameraForwardZ = front;

	//* camera Movement Logic */
	if(currentKey == 'w')
		cameraPosition += cameraSpeed * CameraForwardZ;

	if(currentKey == 's')
		cameraPosition -= cameraSpeed * CameraForwardZ;

	if(currentKey == 'a')
		cameraPosition -= glm::normalize(glm::cross(CameraForwardZ, CameraUpY)) * cameraSpeed;

	if(currentKey == 'd')
		cameraPosition += glm::normalize(glm::cross(CameraForwardZ, CameraUpY)) * cameraSpeed;


	// Transforms the Object
	glm::mat4 model;
	model = glm::translate(model, glm::vec3(0.0f, 0.0f, 0.0f)); // Place the object at the center of the viewport
	model = glm::scale(model, glm::vec3(2.0f, 2.0f, 2.0f)); // Increase the object size by a scale of 2

	//Transforms the camera
	glm::mat4 view;
	glm::vec3 cameraMotion = cameraPosition - CameraForwardZ;

	// constrain camera movement y axis
	if (cameraMotion.y < -89) {
		cameraMotion.y = -89;
	}

	else if (cameraMotion.y > 89) {
		cameraMotion.y = 89;
	}

	// if 2D mode is active sets camera to front view
	if (!isMode3d) {
		cameraMotion.x = 0;
		cameraMotion.z = -20;
		cameraMotion.y = 0;
	}
	view = glm::lookAt(cameraMotion, cameraPosition, CameraUpY);

	// Creates a perspective projection
	glm::mat4 projection;
	projection = glm::perspective(45.0f, (GLfloat)WindowWidth / (GLfloat)WindowHeight, 0.1f, 100.0f);

	// pass calculated matrices to shader program
	modelLoc = glGetUniformLocation(shaderProgram, "model");
	viewLoc = glGetUniformLocation(shaderProgram, "view");
	projLoc = glGetUniformLocation(shaderProgram, "projection");

	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
	glUniformMatrix4fv(projLoc, 1, GL_FALSE, glm::value_ptr(projection));

	// pass object/ light position and color to shader program
	objectColorLoc = glGetUniformLocation(shaderProgram, "objectColor");
	lightColorLoc = glGetUniformLocation(shaderProgram, "lightColor");
	lightPositionLoc = glGetUniformLocation(shaderProgram, "lightPos");
	viewPositionLoc = glGetUniformLocation(shaderProgram, "viewPosition");

	glUniform3f(objectColorLoc, objectColor.r, objectColor.g, objectColor.b);
	glUniform3f(lightColorLoc, lightColor.r, lightColor.g, lightColor.b);
	glUniform3f(lightPositionLoc, lightPosition.x, lightPosition.y, lightPosition.z);
	glUniform3f(viewPositionLoc, cameraPosition.x, cameraPosition.y, cameraPosition.z);

	// sets texture object and triangle limit to draw frame
	glBindTexture(GL_TEXTURE_2D, texture);
	glDrawElements(GL_TRIANGLES, 1000, GL_UNSIGNED_INT, 0);
	glBindVertexArray(0);
	glutPostRedisplay();
	glutSwapBuffers();
}

void UCreateShader(){
	GLint vertexShader = glCreateShader(GL_VERTEX_SHADER); // Creates the vertex shader
	glShaderSource(vertexShader, 1, &vertexShaderSource, NULL); // Attaches the Vertex shader to the source code
	glCompileShader(vertexShader); // Compiles the Fragment shader

	//cube Fragment Shader
	GLint fragmentShader = glCreateShader(GL_FRAGMENT_SHADER); // Create the Fragment shader
	glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL); // Attaches the Fragment shader to source code
	glCompileShader(fragmentShader); // Compiles the Fragment shader

	//cube Shader program
	shaderProgram = glCreateProgram(); // Creates the Shader program and returns an id
	glAttachShader(shaderProgram, vertexShader); // Attach vertex shader to shader program
	glAttachShader(shaderProgram, fragmentShader); // Attach Fragment shader to the Shader program
	glLinkProgram(shaderProgram); //Link Vertex and Fragment shaders to Shader program

	// Delete the vertex and Fragment shaders once linked
	glDeleteShader(vertexShader);
	glDeleteShader(fragmentShader);
}

//creates buffer and array objects
void UCreateBuffers(){
	//position and color data
	GLfloat vertices[] = {
			//position              //normals             //texture coordinates
			//top shelf
			4.0f, 4.0f, 1.0f,       0.0f, 0.0f, 1.0f,    1.0f, 0.0f,  //front right
			4.0f, 4.0f, 2.0f,       0.0f, 0.0f, 1.0f,    1.0f, 1.0f,  //back right
			1.0f, 4.0f, 1.0f,       0.0f, 0.0f, 1.0f,    0.0f, 0.0f,  //front left
			1.0f, 4.0f, 2.0f,       0.0f, 0.0f, 1.0f,    0.0f, 1.0f,  //back left

			//shelf 1
			4.0f, 3.0f, 1.0f,       -1.0f, 0.0f, 0.0f,    1.0f, 0.0f,  //front right
			4.0f, 3.0f, 2.0f,       -1.0f, 0.0f, 0.0f,    1.0f, 1.0f,  //back right
			1.0f, 3.0f, 1.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 0.0f,  //front left
			1.0f, 3.0f, 2.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 1.0f,  //back left

			//shelf 2
			4.0f, 2.0f, 1.0f,		-1.0f, 0.0f, 0.0f,    1.0f, 0.0f,  //front right
			4.0f, 2.0f, 2.0f, 		-1.0f, 0.0f, 0.0f,    1.0f, 1.0f,  //back right
			1.0f, 2.0f, 1.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 0.0f,  //front left
			1.0f, 2.0f, 2.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 1.0f,  //back left

			//shelf 3
			4.0f, 1.0f, 1.0f, 		-1.0f, 0.0f, 0.0f,    1.0f, 0.0f,  //front right
			4.0f, 1.0f, 2.0f,		-1.0f, 0.0f, 0.0f,    1.0f, 1.0f,  //back right
			1.0f, 1.0f, 1.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 0.0f,  //front left
			1.0f, 1.0f, 2.0f, 		-1.0f, 0.0f, 0.0f,    0.0f, 1.0f,  //back left

			//bottom shelf
			4.0f, 0.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 0.0f,  //front right
			4.0f, 0.0f, 2.0f,		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //back right
			1.0f, 0.0f, 1.0f,		0.0f, -1.0f, 0.5f,    0.0f, 0.0f,  //front left
			1.0f, 0.0f, 2.0f, 		0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  //back left

			//right lip
			3.7f, 4.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    0.0f, 0.0f,  //top left
			3.7f, 0.0f, 1.0f,		0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  //bottom left

			//left lip
			1.3f, 4.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 0.0f,  //top right
			1.3f, 0.0f, 1.0f,       0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  // bottom right

			//bottom piece
			4.0f, -0.5f, 1.0f,		0.0f, -1.0f, 0.5f,    1.0f, 0.0f,  //front right
			4.0f, -0.5f, 2.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //back right
			1.0f, -0.5f, 1.0f, 		0.0f, -1.0f, 0.5f,    0.0f, 0.0f,  //front left
			1.0f, -0.5f, 2.0f, 		0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  //back left

			//top lip
			3.7f,  3.7f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //bottom left
			1.3f,  3.7f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // bottom right

			//shelf 1 bottom of lip
			3.7f,  2.8f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //bottom left
			1.3f,  2.8f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // bottom right

			//shelf 2 bottom of lip
			3.7f,  1.8f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //bottom left
			1.3f,  1.8f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // bottom right

			//shelf 3 bottom of lip
			3.7f,  0.8f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //bottom left
			1.3f,  0.8f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // bottom right

			// shelf 1 top of lip
			3.7f,  3.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //top left
			1.3f,  3.0f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // top right

			// shelf 2 top of lip
			3.7f,  2.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //top left
			1.3f,  2.0f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // top right

			// shelf 3 top of lip
			3.7f,  1.0f, 1.0f, 		0.0f, -1.0f, 0.5f,    1.0f, 1.0f,  //top left
			1.3f,  1.0f, 1.0f,      0.0f, -1.0f, 0.5f,    0.0f, 1.0f,  // top right

};

	GLuint indices[] = {
			// top shelf
			0,  1,  2,
			1,  2,  3,

			// shelf 1
			4,  5,  6,
			5,  6,  7,

			// shelf 2
			8,  9,  10,
			9,  10, 11,

			// shelf 3
			12, 13, 14,
			13, 14, 15,

			// bottom shelf
			16, 17, 18,
			17, 18, 19,

			// back panel
			1,  3,  17,
			17, 19, 3,

			// right panel
			0,  1,  16,
			1,  16, 17,

			// left panel
			2,  3,  18,
			3,  18, 19,

			// left lip
			2,  18, 22,
			18, 22, 23,

			// right lip
			0,  20, 21,
			0,  16, 21,

			// bottom piece
			27, 25, 24,
			24, 26, 27,

			// bottom piece back
			19, 17, 25,
			19, 25, 27,

			// bottom piece front
			18, 24, 26,
			16, 18, 24,

			// bottom piece left
			18, 19, 26,
			19, 26, 27,

			// bottom piece right
			17, 24, 25,
			16, 17, 24,

			// top shelf lip
			22,  28, 29,
			20,  22,  28,

			// shelf 1 lip
			36,  37,  30,
			37,  30, 31,

			// shelf 2 lip
			38,  39, 32,
			39, 32, 33,

			// shelf 3 lip
			40, 41, 34,
			41, 34, 35,

			};

	//generate buffer ids
	glGenVertexArrays(1, &VAO);
	glGenBuffers(1, &VBO);
	glGenBuffers(1, &EBO);

	// activate vertex array object
	glBindVertexArray(VAO);

	//activate VBO
	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

	//activate element buffer object/indices
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

	//set attribute pointer 0 to hold position data
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat), (GLvoid*)0);
	glEnableVertexAttribArray(0);

	//set attribute pointer 1 to hold normal data
	glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat), (GLvoid*)(3 * sizeof(GLfloat)));
	glEnableVertexAttribArray(1);

	//set attribute pointer 2 to hold texture data
	glVertexAttribPointer(2, 2, GL_FLOAT,GL_FALSE, 8 * sizeof(GLfloat),(GLvoid*)(6 * sizeof(GLfloat)) );
	glEnableVertexAttribArray(2);

	glBindVertexArray(0);
}


/* Implements the UKeyboard function */
void UKeyboard(unsigned char key, GLint x, GLint y)
{
	switch(key){
	// switches from 2D to 3D
	case 'm':
		isMode3d = !isMode3d;
		cout<<isMode3d<<endl;
		break;

	// zooms in
	case 'w':
		currentKey = key;
		break;

	// zooms out
	case 's':
		currentKey = key;
		break;

	// moves camera left
	case 'a':
		currentKey = key;
		break;

	// moves camera right
	case 'd':
		currentKey = key;
		break;

	default:
		cout<<"Not a valid key!"<<endl;
	}
}

/* Implements the UKeyReleased function */
void UKeyReleased(unsigned char key, GLint x, GLint y)
{
	// clears current key
	currentKey = '0';
}

/* Implements the UMouseClick function */
void UMouseClick(int button, int state, int x, int y){

	// left mouse button held down
	if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN){
		currentMouseButton = GLUT_LEFT_BUTTON;
		return;
	}
	// right mouse button held down
	if (button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		currentMouseButton = GLUT_RIGHT_BUTTON;
		return;
	}

	// clear mouse button click
	currentMouseButton = -1;
}

/* Implements the UKeyboard function */
void UMouseMove(int x, int y)
{

	if (glutGetModifiers() == GLUT_ACTIVE_ALT){
		//Immediately replaces center locked coordinates with new mouse coordinate
		if(mouseDetected)
		{
			lastMouseX = x;
			lastMouseY = y;
			mouseDetected = false;
		}

		if(currentMouseButton == GLUT_LEFT_BUTTON) {

			//gets the direction the mouse was moved in x and y
			mouseXOffset = x - lastMouseX;
			mouseYOffset = lastMouseY - y;//inverted y

			//updated new mouse coordinates
			lastMouseX = x;
			lastMouseY = y;

			//applies sensitivity to mouse direction
			mouseXOffset *= sensitivity;
			mouseYOffset *= sensitivity;

			//accumulates the yaw and pitch
			yaw += mouseXOffset;
			pitch += mouseYOffset;

			//orbits around the center
			front.x = 20.0f * cos(yaw);
			front.y = 20.0f * sin(pitch);
			front.z = sin(yaw) * cos(pitch) * 20.0f;
		}

		// zoom in and out on right mouse held down
		if(currentMouseButton == GLUT_RIGHT_BUTTON) {
			mouseYOffset = y - lastMouseY;

			lastMouseY = y;

			mouseYOffset *= 0.01f;

			cameraPosition.z = cameraPosition.z + mouseYOffset;
		}
	}
}

/* Generate and load texture*/
void UGenerateTexture(){
	glGenTextures(1, &texture);
	glBindTexture(GL_TEXTURE_2D, texture);

	int width, height;

	// use soil library to load in cherry wood grain texture
	unsigned char* image = SOIL_load_image("snhu.jpg", &width, &height, 0, SOIL_LOAD_RGB);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
	glGenerateMipmap(GL_TEXTURE_2D);
	SOIL_free_image_data(image);
	glBindTexture(GL_TEXTURE_2D, 0);
}
