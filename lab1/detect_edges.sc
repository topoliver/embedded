#include <stdio.h>
#include <string.h>
#include <math.h>
import "c_queue";
import "c_bit64_queue";	

typedef  unsigned char uchar;

//
//
//dependency between setup_brightness and susan_edges
behavior SetupBrightnessLut(i_bit64_sender Port)
{
	//NEED to use port to receive bp here
	int   k;
	float temp;
	//hardcode here
	int form = 6;
	int thresh = 20;
	uchar bp[516];//make an array here and use Port to send it out and store in the array
		      //in the array at edgedetect behavior
	bit[64] temp2;

	void main(void)
	{
		for(k=-256;k<257;k++)
  		{
    		  temp=((float)k)/((float)thresh);
    		  temp=temp*temp;
    		  if (form==6)
      		    temp=temp*temp*temp;
    		  temp=100.0*exp(-temp);
    		  //*(*bp[258]+k)= (uchar)temp;
   		  bp[258+k] = (uchar)temp;
  		}
		for(k=0; k <516; k++){
			temp2 = bp[k];
			Port.send(temp2);//send the bp[] out
		}

	}
};

//  memset (mid,100,x_size * y_size);---set mid between two behaviors
//
//setup_brightness_lut(bp,bt,6);
//susan_edges(in,r,mid,bp,max_no_edges);

//uchar bp[516];
//int bt
//uchar in[x_size * y_size];
//int r[x_size * y_size * sizeof(int)];
//uchar mid[x_size*y_size];
//int max_no_edges=2650;

behavior SusanEdgesFirstLoop(in uchar bp[516],in uchar input[76*95],in int x_size,in int y_size,out int r[76*95],in int thread,in int thread_size)
{
  int i,j,n;
  uchar *p,*cp;
  int	max_no=2650;

  void main(void)
  {  
   for (i=3+22*thread;i<22*thread+thread_size+3;i++)
    for (j=3;j<x_size-3;j++)
    {
      n=100;
      p=input + (i-3)*x_size + j - 1;
      cp=bp+258 + input[i*x_size+j];

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-3; 

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-5;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-6;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=2;
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-6;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-5;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-3;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);

      if (n<=max_no)
        r[i*x_size+j] = max_no - n;
    }
  }
};

behavior SusanEdgeSecondLoop(in uchar bp[516],in uchar input[76*95],in int x_size,in int y_size,in int r[76*95],out uchar mid[76*95],in int thread,in int thread_size)
{
  float z;
  int do_symmetry,i,j,m,n,x,y,a,b,w;
  uchar *p,*cp;
  uchar c;
  int	max_no=2650;

  void main(void)
  {
    for (i=4+22*thread;i<22*thread+thread_size+4;i++)
    for (j=4;j<x_size-4;j++)
    {
      if (r[i*x_size+j]>0)
      {
        m=r[i*x_size+j];
        n=max_no - m;
        cp=bp+258 + input[i*x_size+j];

        if (n>600)
        {
          p=input + (i-3)*x_size + j - 1;
          x=0;y=0;

          c=*(cp-*p++);x-=c;y-=3*c;
          c=*(cp-*p++);y-=3*c;
          c=*(cp-*p);x+=c;y-=3*c;
          p+=x_size-3; 
    
          c=*(cp-*p++);x-=2*c;y-=2*c;
          c=*(cp-*p++);x-=c;y-=2*c;
          c=*(cp-*p++);y-=2*c;
          c=*(cp-*p++);x+=c;y-=2*c;
          c=*(cp-*p);x+=2*c;y-=2*c;
          p+=x_size-5;
    
          c=*(cp-*p++);x-=3*c;y-=c;
          c=*(cp-*p++);x-=2*c;y-=c;
          c=*(cp-*p++);x-=c;y-=c;
          c=*(cp-*p++);y-=c;
          c=*(cp-*p++);x+=c;y-=c;
          c=*(cp-*p++);x+=2*c;y-=c;
          c=*(cp-*p);x+=3*c;y-=c;
          p+=x_size-6;

          c=*(cp-*p++);x-=3*c;
          c=*(cp-*p++);x-=2*c;
          c=*(cp-*p);x-=c;
          p+=2;
          c=*(cp-*p++);x+=c;
          c=*(cp-*p++);x+=2*c;
          c=*(cp-*p);x+=3*c;
          p+=x_size-6;
    
          c=*(cp-*p++);x-=3*c;y+=c;
          c=*(cp-*p++);x-=2*c;y+=c;
          c=*(cp-*p++);x-=c;y+=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;
          c=*(cp-*p++);x+=2*c;y+=c;
          c=*(cp-*p);x+=3*c;y+=c;
          p+=x_size-5;

          c=*(cp-*p++);x-=2*c;y+=2*c;
          c=*(cp-*p++);x-=c;y+=2*c;
          c=*(cp-*p++);y+=2*c;
          c=*(cp-*p++);x+=c;y+=2*c;
          c=*(cp-*p);x+=2*c;y+=2*c;
          p+=x_size-3;

          c=*(cp-*p++);x-=c;y+=3*c;
          c=*(cp-*p++);y+=3*c;
          c=*(cp-*p);x+=c;y+=3*c;

          z = sqrt((float)((x*x) + (y*y)));
          if (z > (0.9*(float)n)) /* 0.5 */
	  {
            do_symmetry=0;
            if (x==0)
              z=1000000.0;
            else
              z=((float)y) / ((float)x);
            if (z < 0) { z=-z; w=-1; }
            else w=1;
            if (z < 0.5) { /* vert_edge */ a=0; b=1; }
            else { if (z > 2.0) { /* hor_edge */ a=1; b=0; }
            else { /* diag_edge */ if (w>0) { a=1; b=1; }
                                   else { a=-1; b=1; }}}
            if ( (m > r[(i+a)*x_size+j+b]) && (m >= r[(i-a)*x_size+j-b]) &&
                 (m > r[(i+(2*a))*x_size+j+(2*b)]) && (m >= r[(i-(2*a))*x_size+j-(2*b)]) )
              mid[i*x_size+j] = 1;
          }
          else
            do_symmetry=1;
        }
        else 
          do_symmetry=1;

        if (do_symmetry==1)
	{ 
          p=input + (i-3)*x_size + j - 1;
          x=0; y=0; w=0;

          /*   |      \
               y  -x-  w
               |        \   */

          c=*(cp-*p++);x+=c;y+=9*c;w+=3*c;
          c=*(cp-*p++);y+=9*c;
          c=*(cp-*p);x+=c;y+=9*c;w-=3*c;
          p+=x_size-3; 
  
          c=*(cp-*p++);x+=4*c;y+=4*c;w+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w+=2*c;
          c=*(cp-*p++);y+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w-=2*c;
          c=*(cp-*p);x+=4*c;y+=4*c;w-=4*c;
          p+=x_size-5;
    
          c=*(cp-*p++);x+=9*c;y+=c;w+=3*c;
          c=*(cp-*p++);x+=4*c;y+=c;w+=2*c;
          c=*(cp-*p++);x+=c;y+=c;w+=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;w-=c;
          c=*(cp-*p++);x+=4*c;y+=c;w-=2*c;
          c=*(cp-*p);x+=9*c;y+=c;w-=3*c;
          p+=x_size-6;

          c=*(cp-*p++);x+=9*c;
          c=*(cp-*p++);x+=4*c;
          c=*(cp-*p);x+=c;
          p+=2;
          c=*(cp-*p++);x+=c;
          c=*(cp-*p++);x+=4*c;
          c=*(cp-*p);x+=9*c;
          p+=x_size-6;
    
          c=*(cp-*p++);x+=9*c;y+=c;w-=3*c;
          c=*(cp-*p++);x+=4*c;y+=c;w-=2*c;
          c=*(cp-*p++);x+=c;y+=c;w-=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;w+=c;
          c=*(cp-*p++);x+=4*c;y+=c;w+=2*c;
          c=*(cp-*p);x+=9*c;y+=c;w+=3*c;
          p+=x_size-5;
 
          c=*(cp-*p++);x+=4*c;y+=4*c;w-=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w-=2*c;
          c=*(cp-*p++);y+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w+=2*c;
          c=*(cp-*p);x+=4*c;y+=4*c;w+=4*c;
          p+=x_size-3;

          c=*(cp-*p++);x+=c;y+=9*c;w-=3*c;
          c=*(cp-*p++);y+=9*c;
          c=*(cp-*p);x+=c;y+=9*c;w+=3*c;

          if (y==0)
            z = 1000000.0;
          else
            z = ((float)x) / ((float)y);
          if (z < 0.5) { /* vertical */ a=0; b=1; }
          else { if (z > 2.0) { /* horizontal */ a=1; b=0; }
          else { /* diagonal */ if (w>0) { a=-1; b=1; }
                                else { a=1; b=1; }}}
          if ( (m > r[(i+a)*x_size+j+b]) && (m >= r[(i-a)*x_size+j-b]) &&
               (m > r[(i+(2*a))*x_size+j+(2*b)]) && (m >= r[(i-(2*a))*x_size+j-(2*b)]) )
            mid[i*x_size+j] = 2;	
        }
      }
    }
  }
};


behavior SusanEdges(i_bit64_receiver FromBrightness, i_bit64_sender ToThin)
{
  float z;
  int   do_symmetry, i, j, m, n, a, b, x, y, w;
  uchar c,*p,*cp;
  int x_size = 76;
  int y_size = 95;
//receive in, bp
  uchar input[76 * 95];//use port to receive and store in the in[]
  uchar bp[516];

  int r[76 * 95];
  uchar mid[76*95];
  int	max_no=2650;
 
  //SusanEdgesFirstLoop(bp,input,x_size,y_size,r,thread,thread_size); 
  SusanEdgesFirstLoop Sefl1(bp,input,x_size,y_size,r,0,22);
  SusanEdgesFirstLoop Sefl2(bp,input,x_size,y_size,r,1,22);
  SusanEdgesFirstLoop Sefl3(bp,input,x_size,y_size,r,2,22);
  SusanEdgesFirstLoop Sefl4(bp,input,x_size,y_size,r,3,23);  

  //SusanEdgeSecondLoop(bp,input,x_size,y_size,r,mid,thread,thread_size)
  SusanEdgeSecondLoop Sesl1(bp,input,x_size,y_size,r,mid,0,22);
  SusanEdgeSecondLoop Sesl2(bp,input,x_size,y_size,r,mid,1,22);
  SusanEdgeSecondLoop Sesl3(bp,input,x_size,y_size,r,mid,2,22);
  SusanEdgeSecondLoop Sesl4(bp,input,x_size,y_size,r,mid,3,21);

  void main(void)
  {
  int k;
  bit[64] temp;
    //receive in and bp here
    for(k=0;k<7220;k++){
      FromBrightness.receive(&temp);
      input[k] = temp;
    }
    for(k=0;k<516;k++){
      FromBrightness.receive(&temp);
      bp[k] = temp;
    }

    memset (mid,100,x_size * y_size);//this is from susan.c main()
  //original
    memset (r,0,x_size * y_size * sizeof(int));
    //First data parallel appplication
    par{
      Sefl1.main();
      Sefl2.main();
      Sefl3.main();
      Sefl4.main();
    }
  //  memcpy(r,r1,76*22* sizeof(int));
  //  memcpy(&r[76*22],r2,76*22* sizeof(int));
  //  memcpy(&r[2*76*22],r3,76*22* sizeof(int));
  //  memcpy(&r[3*76*22],r4,76*23* sizeof(int));
    //second data parallel appplication
    par{
      Sesl1.main();
      Sesl2.main();
      Sesl3.main();
      Sesl4.main();
    }

    //do not need to send bp anymore
    for(k=0;k<7220;k++){
      temp = input[k];
      ToThin.send(temp);
    }
    for(k=0;k<7220;k++){
      temp = mid[k];
      ToThin.send(temp);
    }
    for(k=0;k<7220;k++){
      temp = r[k];
      ToThin.send(temp);
    }
  }//void main of behavior
};


//encapsulate in detectedges
//the start is the signal from file input, the c_queue Port is the image buffer
//is there any build-buffer that better than c_queue
behavior DetectEdges(i_bit64_receiver Portin, i_bit64_sender Portout)
{
	//make port for buffer and communication
	const unsigned long sizebrightness = 516;//is it ok..?
	const unsigned long sizeedges = 43320;//is it ok..?
	int k = 0;
	bit[64] temp;
	c_bit64_queue		c_brightness(sizebrightness);
	c_bit64_queue		c_edges(sizeedges);
	SetupBrightnessLut	setup_brightness_lut(c_brightness);
	SusanEdges	susan_edges(c_edges, Portout);


	//bp is the array that brightness and edges will use	
	uchar bp[516];
	//r and mid buffer for edges use, and send to susanThin by Portout
	uchar input[76 * 95];//use port to receive and store in the in[]
	int r[76*95]; //mid and r and in will get updated by susan_edges
	uchar mid[76*95];//in and bp have to be sent to susan_edges

//the r is the only problem

	void main(void)
	{
		//receive from SUSAN
		for(k=0;k<7220;k++)
		{
			Portin.receive(&temp);//receive the whole uchar in[] array here?????
			input[k] = temp;
		}
		//the process of detectEdges
		//setup brightness
		setup_brightness_lut.main();
		for(k=0;k<516;k++){
			c_brightness.receive(&temp);//receive all of the bp in one time???
			bp[k] = temp;
		}
		//send in and bp for edges use
		for(k=0;k<7220;k++){
			temp = input[k];
			c_edges.send(temp);
		}
		for(k=0;k<516;k++){
			temp = bp[k];
			c_edges.send(temp);
		}		
		//edges process
		susan_edges.main();
		//update in, r, mid
	}
};



