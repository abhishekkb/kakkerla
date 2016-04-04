package com.example.slotmachine;

// 4-sided cube
public class Spinner extends Mesh {
	public Spinner(float width, float height, float depth) {
        width  /= 2;
        height /= 2;
        depth  /= 2;
 
        float vertices[] = {
        		-width, -height, -depth, // [0]  point 0  back
                 width, -height, -depth, // [1]  point 1
                 width,  height, -depth, // [2]  point 2
                -width,  height, -depth, // [3]  point 3
                             
                 width,  height, -depth, // [4]  point 2 top
                -width,  height, -depth, // [5]  point 3
                 width,  height,  depth, // [6]  point 6
                -width,  height,  depth, // [7]  point 7
                 
                -width, -height,  depth, // [8]  point 4 front
                 width, -height,  depth, // [9]  point 5
                 width,  height,  depth, // [10] point 6
                -width,  height,  depth, // [11] point 7
                             
                -width, -height, -depth, // [12] point 0 bottom
                 width, -height, -depth, // [13] point 1
                -width, -height,  depth, // [14] point 4 
                 width, -height,  depth, // [15] point 5        
        };
        
        
        short indices[] = { 
        		0,2,1, 		// back
        		0,3,2,
        		7,4,5,		// top
        		7,6,4,
        		8,10,11,	// front
        		8,9,10,
        		14,12,13,	// bottom
        		14,13,15
        };
     
        // Mapping coordinates for the vertices - this array needs to be same size as vertices array
		float textureCoordinates[] = { 
				0.5f, 0.5f,	// vertex [0] back
				1.0f, 0.5f,	// vertex [1]
				1.0f, 1.0f,	// vertex [2]
				0.5f, 1.0f,	// vertex [3]
				
				1.0f, 0.0f,	// vertex [4] top
				0.5f, 0.0f, // vertex [5]
				1.0f, 0.5f, // vertex [6]
				0.5f, 0.5f, // vertex [7]
				
				0.0f, 0.5f, // vertex [8] front
				0.5f, 0.5f, // vertex [9]
				0.5f, 0.0f, // vertex [10]
				0.0f, 0.0f, // vertex [11]
				
				0.0f, 1.0f, // vertex [12] bottom
				0.5f, 1.0f, // vertex [13]
				0.0f, 0.5f, // vertex [14]
				0.5f, 0.5f, // vertex [15]
		};   
        
	    setIndices(indices);
        setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
    }
	public Spinner(float radius, float width){
		float l = width/2;
		float r = radius;
		float a = (float) (0.4*Math.PI); // angle
		float pi = (float) Math.PI; 
		float cos_a = (float) Math.cos(0.4*Math.PI);//(float)Math.cos(a);//(float) 0.30901699437;//
		float sin_a = (float) Math.sin(0.4*Math.PI);//(float)Math.sin(a);//(float) 0.95105651629;//
		float cos_aby2 = (float) Math.cos(0.2*Math.PI);//(float)Math.cos(a/2);//(float) 0.80901699437;//
		float sin_aby2 = (float) Math.sin(0.2*Math.PI);//(float)Math.sin(a/2);//(float) 0.58778525229;//
		
		float vertices[] = {
			-l, -r*sin_aby2,  r*cos_aby2,	//i[0]	//v[0]	// face 1
			 l, -r*sin_aby2,  r*cos_aby2,	//i[1]	//v[1]
			 l,  r*sin_aby2,  r*cos_aby2,	//i[2]	//v[3]
			-l,  r*sin_aby2,  r*cos_aby2,	//i[3]	//v[2]
			
			-l,  r*sin_aby2,  r*cos_aby2,	//i[4]	//v[2]	// face 2
			 l,  r*sin_aby2,  r*cos_aby2,	//i[5]	//v[3]
			 l,  r*sin_a   , -r*cos_a   ,	//i[6]	//v[5]
			-l,  r*sin_a   , -r*cos_a   ,	//i[7]	//v[4]
			
			-l,  r*sin_a   , -r*cos_a   ,	//i[8]	//v[4]	// face 3
			 l,  r*sin_a   , -r*cos_a   ,	//i[9]	//v[5]
			 l,  0         , -r         ,	//i[10]	//v[7]
			-l,  0         , -r         ,	//i[11]	//v[6]
			
			-l,  0         , -r         ,	//i[12]	//v[6]	// face 4
			 l,  0         , -r         ,	//i[13]	//v[7]
			 l, -r*sin_a   , -r*cos_a   ,	//i[14]	//v[9]
			-l, -r*sin_a   , -r*cos_a   ,	//i[15]	//v[8]
			
			-l, -r*sin_a   , -r*cos_a   ,	//i[16]	//v[8]	// face 5
			 l, -r*sin_a   , -r*cos_a   ,	//i[17]	//v[9]
			 l, -r*sin_aby2,  r*cos_aby2,	//i[18]	//v[1]
			-l, -r*sin_aby2,  r*cos_aby2	//i[19]	//v[0]
		};
		short indices[] = {
			0,2,3,	 //v-0,3,2	// face 1
			0,1,2,	 //v-0,1,3
			
			4,6,7,	 //v-2,5,4,	// face 2
			4,5,6,	 //v-2,3,5,
			
			8,10,11, //v-4,7,6,	// face 3
			8,9,10,  //v-4,5,7,
			
			12,14,15,//v-6,9,8,	// face 4
			12,13,14,//v-6,7,9,
			
			16,18,19,//v-8,1,0,	// face 5
			16,17,18 //v-8,9,1	
		};
		float textureCoordinates[] = {
			 0.00f, 0.33f,	//i[0]	//v[0]	// face 1
			 0.33f, 0.33f,	//i[1]	//v[1]
			 0.33f, 0.00f,	//i[2]	//v[2]
			 0.00f, 0.00f,	//i[3]	//v[3]
			
			 0.33f, 0.33f,	//i[4]	//v[2]	// face 2
			 0.66f, 0.33f,	//i[5]	//v[3]
			 0.66f, 0.00f,	//i[6]	//v[4]
			 0.33f, 0.00f,	//i[7]	//v[5]
			
			 0.66f, 0.33f,	//i[8]	//v[4]	// face 3
			 1.00f, 0.33f,	//i[9]	//v[5]
			 1.00f, 0.00f,	//i[10]	//v[6]
			 0.66f, 0.00f,	//i[11]	//v[7]
			
			 0.00f, 0.66f,	//i[12]	//v[6]	// face 4
			 0.33f, 0.66f,	//i[13]	//v[7]
			 0.33f, 0.33f,	//i[14]	//v[8]
			 0.00f, 0.33f,	//i[15]	//v[9]
			
			 0.33f, 0.66f,	//i[16]	//v[8]	// face 5
			 0.66f, 0.66f,	//i[17]	//v[9]
			 0.66f, 0.33f,	//i[18]	//v[0]
			 0.33f, 0.33f	//i[19]	//v[1]
	
		};
		setIndices(indices);
        setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
	}
}
