#include <stdio.h>
#include <string.h>
import "c_queue";

//receive in, mid
behavior EDGE_DRAW(i_receiver Portin, i_sender Portout)
{

void main(void)
{
int   i;
uchar *inp, *midp;
int x_size = 76, y_size = 95;
int drawing_mode = 0;
uchar in[x_size * y_size];
uchar mid[x_size*y_size];

  Portin.receive(in, );
  Portin.receive(mid, );

  if (drawing_mode==0)
  {
    /* mark 3x3 white block around each edge point */
    midp=mid;
    for (i=0; i<x_size*y_size; i++)
    {
      if (*midp<8) 
      {
        inp = in + (midp - mid) - x_size - 1;
        *inp++=255; *inp++=255; *inp=255; inp+=x_size-2;
        *inp++=255; *inp++;     *inp=255; inp+=x_size-2;
        *inp++=255; *inp++=255; *inp=255;
      }
      midp++;
    }
  }

  /* now mark 1 black pixel at each edge point */
  midp=mid;
  for (i=0; i<x_size*y_size; i++)
  {
    if (*midp<8) 
      *(in + (midp - mid)) = 0;
    midp++;
  }
  //send in out
  Portout.send(in, );
}//main
};

