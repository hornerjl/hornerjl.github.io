# **Self Assesment**
The Capstone for the computer science program was a great experience which allowed me to be able to take work from previous classes in this program and refine them for an optimal program adding refinements that I’ve picked up along the way. During this program I learned a lot about the many aspects of software design and engineering including UML design, code reviews, openGL, database operations, data structures, and reverse engineering. The capstone allowed me to not only use the particular class the assignment was from but also from the other classes to add to my projects concepts from a more in-depth knowledge base. 

<details>
    <summary>Click here for more!</summary>

    I think one of the biggest things that sets me apart in my e portfolio is the code review. In the field being able to review code is one of the biggest tasks that can make or break a project. My code review took each project line by line (as seen in the video transcript) and walked through the code and what could be improved. This shows competence in being able to work with others reviewing their code and explaining to them the reasons why things need fixed and how to go about it. I feel that another element that demonstrates my engineering skills is the practicality of these examples to real world projects. The RESTful API project in particular is an example of a very important aspect of web development: the communication between and separation of backend and frontend. Additionally this project as well as the openGL project demonstrate working with modern concepts like GLSL and NoSQL databases.
	Being able to complete and expand upon these projects throughout the program and showcasing them on an eportfolio definitely helped show the strengths I have. This also helped me see the things I have enjoyed doing more than others allowing me to better decide the path in software engineering I want to take. I have learned the application of many concepts and languages which I feel gives me a competitive edge over someone with a more limited focus. I had to be able to move from one development stack to the next which shows my adaptability in the field and my ability to quickly pickup and learn languages and concepts needed for a project. For example, in the class on software testing, I had to pick up skill with Java class structure and Junit testing suites in a relatively short span of time to complete the course. Similarly in the course on reverse engineering, I had to pick up languages like GAS assembly and C in a short period of time while also mastering use of Bless and GDB to take a binary and manually decompile it into the exact C program it was compiled from. The speed of modern development can leave little time for learning concepts so generally any gap in knowledge has to be filled in quickly and with a focus on real world application. 
<br/>
	My skills with collaboration and communication include knowledge of both Agile and Waterfall development structures with a focus on Scrum sprint structure with its focus on stories and shorter development times for smaller pieces of the full project design and on design documents such as sequence diagrams and use case diagrams. Additionally I have conducted multiple code reviews both for improvements to my own code and for improvements to other students code offering suggestions for improving functionality and quality. In addition to this top layer of design and communication principles, I have also honed a set of skills with day to day software challenges including the implementation of data structures like hash tables and binary trees, use of object-oriented principles such as encapsulation and abstraction, and implementing concepts such as unit tests, token based access, and schemas all with an eye on secure and controlled access of data. 
<br/>
	What follows is a broad representation of my work for these courses starting with a code review demonstrated on the rough drafts of each program contained in this portfolio. This code review is meant to illustrate the cycle of implementation, review, and revision that is an essential part of software development. Additionally it showcases my improvement over time with clear rough implementations that become cleaned up and more fully realized in the final versions below. The first program after the review is my implementation of a basic server backend showcasing knowledge of working with a database, in this case a NoSQL database, as well as concepts of controlled access and validating user input. It uses a simple token system after receiving a user name and password which allows access to the rest of the routes in this python-based RESTful API using Bottle. Next is a demonstration of modern OpenGL to create a simple bookshelf object that can be viewed using controls for rotating and positioning the camera. This object is built using the OpenGL Shading Language(GLSL) in conjunction with C++ to shape, shade, texture, and light the bookshelf object. Last is a hash table implementation with collision handling to allow for storage of a large amount of data in a quickly and flexibly searchable format using a built from scratch hash table that uses the unordered_map, array, and vector structures to hold complex data while still allowing for easy access of data using a simple hash function that converts a string to an int and then uses modulo to assign a placement in the array and later to retrieve the index of that placed object.
 

</details>

## **Code Review Before Final Code Revision**

[![Code review video](https://img.youtube.com/vi/Ok3d9NTWEYE/0.jpg)](https://www.youtube.com/watch?v=Ok3d9NTWEYE)

<details>
    <summary>Click here for video transcript</summary>

Modern OpenGL Bookshelf 3D Model
<br/>
<br/>


The purpose of the first artifact is to render a 3d bookshelf object using modern openGL complete with texture and lighting. Additionally mouse and keyboard controls are implemented to allow for a full 360 view of the object.
<br/>
<br/>

Lines 1-11
<br/>
<br/>

In addition to the standard namespace and iostream library, we load in libraries that act as helpers to simplify our code for interfacing with openGL including GLM for math, glew/freeglut for cleaner code, and a locally stored SOIL2 library for easily loading textures in a usable form for openGL objects.
<br/>
<br/>

Lines 14-62
<br/>
<br/>

Next we define some macros and globals that will be required in multiple places throughout our program. This is a good area for some code upgrades as it contains a lot of magic numbers and non-constant values that would be better encapsulated by an instance of a class. Additionally several of these values will be removed entirely as we cut down to one light source. 
<br/>

Lines 65-98
<br/>
<br/>

Here we define a GLSL shader that will take in our geometry data and render the basic bookshelf based on the output of its own main function which performs calculations and then passes along the data to be rendered by openGL. While there is some annotation, some of it could be improved to be more descriptive of the actual function of each matrix and vector to add to readability.
<br/>
<br/>

Lines 100-171
<br/>
<br/>

The next shader handles lighting and texturing calculations and similarly could use some extra documentation and the removal of line 122 which is not necessary for the function of the program. Additionally this function will be edited to remove all references to a second light source which will remove the repetition though alternately if we needed the second light we could probably stream line this with a separate function since the calculations performed on each light is quite similar.
<br/>
<br/>

Lines 174-216
<br/>
<br/>

The main function takes in any command line args and passes them along to our openGL initializing function. Then it calls all necessary startup functions before running the glutMainLoop function which keeps the program active. Then it performs some manual cleanup of openGL buffer objects before exiting. Again there is a lot of function calls here that could benefit from annotation on their purpose for the program.
<br/>
<br/>

Lines 218-222
<br/>
<br/>

Next is a small function that instructs openGL how to handle a window reassigning two global variables and passing that along to an openGL function that adjusts the viewpoint. Again comments and better encapsulation will improve the structure of this function making it part of a controller object. 
<br/>
<br/>

Lines 224-315
<br/>
<br/>

URenderGraphics is quite long as a function and handles a lot of responsibility therefore it would be better split into multiple pieces that handle some of the calculations and conditional camera positioning. In general, this function handles the input to our shaders conveying all necessary information in one place. Additional extra documentation would help clarify some of the design decisions here as well. This function will be simplified somewhat by the removal of position data for light source 2.
<br/>
<br/>

Lines 317-336
<br/>
<br/>

UCreateShader uses the data from our two shaders to initialize an active shader program that utilizes the data calculated to draw the object. This function is well documented and does garbage collection for the shader information once the shader program is initialized.
Lines 339-493
<br/>
<br/>

UCreateBuffers handles the actual calculations for the vertices and indices that make up the shape of the bookcase as well as shading and texture coordinates for each vertex. The vertices are broken down by location but the indices could use some comments detailing what they correspond to as well. Additionally some of the comments are out of date and erroneous for this version of the program. A lot of the update work will be focused on fixing and expanding the vertex values to fix various clipping issues due to overlapping values as well as adjusting the normals column to create a more natural effect from the lighting on the object.
<br/>
<br/>

Lines 497-535
<br/>
<br/>

These two functions instruct the openGL instance on how to handle key presses and releases. The  console prints are testing code that should be cleaned out other than the default case for our switch statement which if tweaked to say something like “key press invalid” will serve to inform the user that their key press is not a recognized key.  
<br/>
<br/>

Lines 537-597
<br/>
<br/>

The next two functions serve as handlers for detected mouse movement and input. These along with the keyboard functions could be rolled into a class with their reliance on global variables that all four use. UMouseClick lacks annotations and UMouseMove could use a few as well clarifying some of the math performed. 
<br/>
<br/>

Lines 600-613
<br/>
<br/>

This last function does the texture loading and uses openGL methods to bind the loaded texture to the openGL instance. There are multiple unnecessary prints to the console to be cleaned up as well as some additional annotation work to be done. 
<br/>
<br/>
<br/>

CSV To Hashtable Converter For Auction Data
<br/>
<br/>

The purpose of this second artifact is to demonstrate a hash table with collision handling that can store and look up information about information loaded in from a csv file specially tuned to take input from an auction listing csv.
<br/>
<br/>

Lines 7-16
<br/>
<br/>

We start with some fairly standard C++ includes. Additionally we load in a locally stored CSV Parser library to take advantage of external resources and focus on just the hash table structure. Additionally as this is a fairly compact program with a small dependency list, we use the std namespace for simplicity.
<br/>
<br/>

Lines  22-36
<br/>
<br/>

Here we have some global definitions including a const that determines the size of the hash table which could possibly be moved to a static constant class member for the hash table class below instead. Additionally we have a declaration for a struct called Bid that mimics the necessary information from a single row of the CSV. This will require a few more members to cover the new requirements including properties for closing date, paid date, winning bid, and auction title. 
<br/>
<br/>

Lines 46-60
<br/>
<br/>

Next is the class definition for our hash table class which contains a private pointer member that points to a vector of Bid structs. Additionally we have a private function called hash which is only for use by the public facing Insert function. Finally we have declarations for a constructor, destructor, and functions covering insertion, deletion, searching and also a function that prints all members. This class will need to be upgraded to include a sortBy member and parameters for the search and remove functions will have to be renamed to suit their new roles.
<br/>
<br/>

Lines 65-74
<br/>
<br/>

Moving on to the definitions for the constructor and destructor, we initialize the previously declared pointer to point at an array of vectors with the Bid typing to create our hash table data structure. This 3-dimensional structure handles collisions between hashed bids by simply pushing them into the same vector. Additionally the destructor properly deallocates the memory used by this pointer afterwards. An addition will need to be made here for a passed in sortBy member.
<br/>
<br/>

Lines 76-156
<br/>
<br/>

Now we come to the class function definitions declared earlier. Each one is annotated with parameters, return values(if they’re not void), and a brief description of their purpose. First we have the hash function that takes in a key and performs the hashing operation returning a valid index for the bunchOfBids table. Insert takes in a Bid struct and after converting the necessary identifier to an int passes it down to the hash function using the returned index to place the Bid in the data structure. This will be modified to handle more types of data depending on the needs of the new Bid struct members but will fundamentally perform the same role with each sortBy type.
<br/>
<br/>

PrintAll simply iterates over both dimensions of the data structure using 2 for loops and does a formatted print out of each individually hashed Bid and its place in the structure. Both of these loops are finite and rely on finite known quantities, one a constant value and the other being based on the size of each allocated for vector in our data structure. Remove takes in a string identifier and does the same conversion hashing operation that Insert does which is a possible area for streamlining by moving the conversion step to the hash function to cut down on repetition. The remove function then uses the hashed index number to locate the correct vector and iterates through the Bids inside calling erase if the vector erase function on the matching bid. Search returns a Bid using much the same process as the delete function leading to the potential that this iterator block could be moved to a separate function since only the end action is unique.
<br/>
<br/>

Lines 167-211
<br/>
<br/>

Next are methods that support the function of the hash table by prepping or displaying formatted data as a bridge for the csv to Bid struct conversion and proper display of searched bids. The first function is display bids which simply takes in a bid and prints the contents to the console. This will need to be expanded to include the planned new members of the Bid struct. Additionally this might be better organized as part of the Bid struct. LoadBids takes in the file system path of the csv file and a pointer to a hash table object. It implements the imported CSVParser class adding a printout of detected headers which might be better removed from the function and added to another function who’s purpose is just to get and print headers. Then it moves on to the meat of the function which creates a bid and populates it with data from the csv file. This loop runs until the iterator is equal to the number of rows in the csv ensuring every entry is read. The bid creation process is slightly cumbersome as instead of loading in everything with the constructor all properties are manually assigned one by one. Therefore the Bid constructor should be upgraded to handle assignment internally. This process also utilizes an unnecessary call of strToDouble for the passed in value to bid amount even though at no point is amount utilized as a double. This string to double will be phased out as it is unnecessary. The final step is to insert each constructed bid into our hash table using the Insert function. This csv to Bid loop is surrounded with a try catch to handle possible errors that might arise from the conversion process.
<br/>
<br/>


Lines 229-313
<br/>
<br/>

Last is the main function which currently handles all user input. This function is quite long and could be broken into pieces like processCLargs(Jamie Note: highlight 232-245), displayMenu(Jamie Note: highlight 257-264), callCSVLoader(Jamie Note: highlight 269-281), and displayRequestedBid(Jamie Note: highlight 288-302). Also the load bids input should have an additional numbered prompt for sortBy as our new hash table will need this property. Additionally the main user input switch case and the new sort by switch case to be added could benefit from a default statement that displays a message to the user that their input is invalid.
<br/>
<br/>
<br/>

RESTful API Server Using MongoDB
<br/>
<br/>

The purpose of the third artifact is to create a Python based server that exposes a RESTful API that when started up allows the user to make calls to perform CRUD operations on a mongoDB database. This database contains information about the performance of various companies and the API includes several functions for performance reporting.
<br/>
<br/>

Lines 1-12
<br/>
<br/>

The script starts by importing libraries for our API including parts of bottle for handling incoming HTTP requests and pymongo for interfacing with our Mongo database. There is a slight redundancy of importing the whole bottle library and then importing only parts of it the next line down. Next is the definition of a connection to a mongoDB client which currently points to localhost but which will be upgraded to instead point at a hosted database through MongoDB’s cloud service Atlas. Then it requests and stores the market database and the stocks collection. This will be expanded to include an access collection which will be checked for user credentials before issuing a short login token to the user. 
<br/>
<br/>

Lines 15-40
<br/>
<br/>

These lines contain test code which will be removed for the final project but which served as useful tests of connectivity. Instead the first routes will be the login route and the token protected create login route. This will ensure database access is controlled and only someone previously authorized can add new credentials. The login route will execute a find query on the access collection and if it finds a match for the query params then it will create a new token object with a timestamp and randomized id returning the id to the user. Failing to find a match it will return a string reporting this to the user. All subsequent call handlers will implement a function to check the headers of the API call for an id and this will be matched against existing token objects to see if it matches a non-expired one.
<br/>
<br/>

Lines 42-91
<br/>
<br/>

The following functions implement client facing CRUD database operations to the user. Structurally they are all fairly simple taking in a request object provided by bottle with the headers, params, and body of the HTTP request. The necessary data is extracted then it tries a corresponding built in pymongo action using this data returning an error or a statement of success. There are a couple areas of opportunity for these functions including pre-processing the data for empty or erroneously formatted request parameters and returning the data found to the client instead of just printing it in the server instance log. Additionally there are several unnecessary print statements logging data for testing purpose that should be removed as they are unnecessary. (line 47)Additionally the create route does not pass along the reason for failure instead just returning that the operation failed. Lastly the delete and update routes use a GET request while they would more accurately be a DELETE and PUT respectively.
<br/>
<br/>

Lines 93-123
<br/>
<br/>

Taking the last 2 functions, the first function does an aggregate query for a particular industry query parameter returning the top 5 performers for that industry. Again the query parameter could use validation and the results are just printed to the server logs instead of returned to the client and in this case there is no error handling try-catch block which would be a worthwhile upgrade to make. The report route collates multiple requested objects into one result and passes along the response. Here too the upgrades of validation and returning vs. simply logging the result are necessary upgrades.
</details>

## **RESTful API Server Using MongoDB**
### Narrative
This artifact is a RESTful API server running on the bottle python framework that serves as a middleware for performing database operations on a mongoDB database hosted on mongoDB’s own Atlas cloud service. It was created for the course CS 340 as a culmination of using python scripts and bash commands to perform operations on a mongoDB database. The data set stored in the collection is supposed to be a set of data related to stock market data for various companies. It originally had routes for basic CRUD operations, a route for performing an aggregation that gets the top 5 performers for a particular industry, and a route for getting a list of different stock entries by ticker values.
<details>
    <summary>Click here for more</summary>

    My selection of this artifact is based on it demonstrating skill with modern frameworks and making use of a NoSQL database structure while still enforcing some of the structure found in a SQL database by way of the API. A major improvement implemented is forcing api calls to follow a JSON schema allowing for required fields and also in some cases disallows additional properties to be sent up. This enforces a flexible level of uniformity for data entering the databases. Additionally the API is now access controlled showcasing basic access control for an API using a UUID as a token. These tokens can only be obtained via a login route that requires a JSON object containing only a username string and a password string. 
   <br/>
    All objectives from the enhancement plan have been met in addition to enhancements suggested in the code review. A route for logging in has been added along with an additional route for adding new credentials which is access protected by the same token system that now protects all routes. In addition to access control, the database is now hosted by MongoDB Atlas which is a portable cloud based database service which fulfills the other major goal from the initial upgrade plan. For improvements from the code review, each route has comments detailing its purpose and what parameters it takes in and jsonschema is used to create validation functions that make sure that the data received is correctly formatted before attempting to perform database operations with it.
   <br/> 
    One of the major challenges was constructing a way to validate something as open ended as JSON. Unlike SQL with its strict database structures, MongoDB is generally a lot more fluid and this requires planning around so that incorrectly formatted data doesn’t get added and break expected returns from future queries. Finally settling on jsonschema seemed like a good fit as it has a natural flow to designing its structures that mimics the exact way I expect the data to look and then compares that with what is actually received surfacing any violations to the end user in human readable terms. Additionally setting up a MongoDB client that could connect with the hosted database took some debugging as access is very specifically controlled and the api call can be a bit tricky to get quite right but doesn’t necessarily throw any errors if formatted incorrectly. Overall this was a good lesson in constructing a controlled access RESTful API that connects to and performs operations on a mongoDB database.
</details>

<br/>
[Project code can be found here!](https://github.com/hornerjl/hornerjl.github.io/tree/master/database)

## **Modern OpenGL Bookshelf 3D Model**
### Narrative
This artifact is demonstrated by the 3D model of a bookshelf. This was done in the C++ language using openGL with freeglut. This project was created 2 months ago as a project done for the course CS 330 Computational Graphics and visualization which focused on 3D graphics software development and openGL as a platform. This project was included as it was part of the core courses  from the recommended courses for the final project for this particular artifact. This was included into my eportfolio because it was something that was designed from scratch by me including the arithmetic done for the various calculations needed including but not limited to the light source, vertices, and camera/object positioning.
<details>
    <summary>Click here for more</summary>

    This is a Project I am very proud of and consider it to be one of the more valuable learning experiences as I was able to make the environment and object from the ground up. The specific components of the artifact that showcases my skills and abilities in software development are the use of the C++ language, utilization of multiple libraries, working with low level 3D rendering platform. 
    While creating this artifact I got a better sense of the c++ language and how it can be utilized to create more than just command line based programs. While this is built on a lot of encapsulation and abstraction, it was none the less a useful example of C++ in action. Additionally this was a useful introduction to the intersection between math and programming requiring knowledge of both in equal measure. A major challenge I faced early on was trouble with the C++ library structure. While I have a much better understanding of the roles of header, dll, and library files now, it was difficult to understand the required structure compared to some more modern languages with their package managers that do all of the linking and pathing for you. Additionally drawing out all of the vertices and how they related to each other proved challenging for a more complex object like a bookshelf and was ultimately solved by drawing it all out on a whiteboard. There wasn’t much feedback required for the bookshelf but I still took the time to add some polish like better lighting and getting rid of certain instances of individual triangles clipping into one another. As the object in question is a bookshelf there was a lot of overlap where the shelf meets the outer frame. This had to be solved by changing values to be adjacent rather than overlapping.
</details>

<br/>
[Project code can be found here!](https://github.com/hornerjl/hornerjl.github.io/tree/master/bookshelf)

## **CSV To Hashtable Converter For Auction Data**
### Narrative
This artifact is a hash table that uses an array of vectors to hold auction data from a csv created for the CS 260 class on data structures and algorithms. The hash function takes a string and turns it into a number then performs a modulo operation on it to reduce it to the index location of one of the vectors in the array. Since a vector is the base unit of the hash table, it can be used to hold multiple items thus removing the danger of collisions from identical hashed values. Lookups for searching and deletion are done by running the same math equation to get the index and then iterating through the vector stored at that location til all matches for the search term are found. 
<details>
    <summary>Click here for more</summary>
  
    This artifact was selected as it showcases skill with hash tables, one of the most powerful data structures for quick lookup times and also utilization of several other structures working together to create a bigger system including unordered maps, vectors, and arrays. These each provide benefits and compliment each other to create a very efficient lookup table that is still rather flexible. The improvements to the structure include an expanded data set for individual items, a flexible hashing algorithm that can hash any string passed into it allowing for sorting based on multiple headings, and an updated UI that provides the user more control over the hash table operations.
	The planned enhancements for this artifact were increased object fields and allowing the user to select their own field to sort by from an abridged list of the headers from the csv file. These two were implemented as planned with several additional improvements from the code review including better encapsulation and application of the single responsibility principle to the structure of the program. While this still covers the same course outcomes, these enhancements lead to a more polished artist overall.
<br/>
	The process of enhancing the artifact certainly helped deepen my knowledge of C++’s eccentricities in particular and program structure as a whole. One challenge in particular was a bug that cropped up when assigning a new hashTable object to a passed in pointer from the main function. Since this takes place outside of the original scope, the pointer was actually altered to point to a different location than the original passed in memory location. Realizing this led me to refactoring to make the instance of the object and just use a setter for the sortBy property that had led to the new keyword usage in the first place. Another issue was finding a structure that would allow for dynamic object property access based on user input which led me to the unordered_map which allows for accessing properties by their variable names. Overall this was a great opportunity to sharpen my skills with basic program structure and utilize pre-existing data structures and libraries to perform a complex task in a simple way.
</details>

<br/>
[Project code can be found here!](https://github.com/hornerjl/hornerjl.github.io/tree/master/hashtable)
