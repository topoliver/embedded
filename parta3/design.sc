#include "susan.sh"

//import "susan";
import "read_image";
import "write_image";
//import "c_uchar7220_queue";
import "c_uchar7220read_queue";
import "c_uchar7220write_queue";

import "input";
import "output";
import "pe1";
import "os1";
behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{
	OS os;
    c_uchar7220read_queue in_image(1ul, os);
    c_uchar7220write_queue out_image(1ul, os);
    
    //ReadImage read_image(start, image_buffer, in_image);
    //Susan susan(in_image, out_image);
    //WriteImage write_image(out_image, out_image_susan);
	INPUT input(start, image_buffer, in_image);
	PE1 pe1(in_image, out_image, os);
	OUTPUT output(out_image, out_image_susan);

    void main(void) {
       par {
            input.main();
            pe1.main();
            output.main();
        }
    }
    
};
