#include "rsocket.h"
#include<stdio.h> //printf
#include<string.h> //memset
#include<stdlib.h> //exit(0);
#include<arpa/inet.h>
#include<sys/socket.h>
#include <fcntl.h> // for open
#include <unistd.h> // for close
 
#define SERVER "127.0.0.1"
#define PORT 6000   //The port on which server recieves data
 
void die(char *s){
    perror(s);
    exit(1);
}
 
int main(void){
	
	clear();
    struct sockaddr_in sAddr;
    int s, i, slen=sizeof(sAddr);
    char buf[BUFLEN];
    char message[BUFLEN];
 
    if ( (s = r_socket(AF_INET, SOCK_BRP, 0)) == -1){
        die("socket");
    }

	
    memset((char *) &sAddr, 0, sizeof(sAddr));
    sAddr.sin_family = AF_INET;
    sAddr.sin_port = htons(PORT);
     
    if (inet_aton(SERVER , &sAddr.sin_addr) == 0) {
        fprintf(stderr, "inet_aton() failed\n");
        exit(1);
    }
 
    while(true){
        printf("Enter message : ");
        scanf("%s",message);
         
        //send the message
        if (r_sendto(s, message, strlen(message) , 0 , (struct sockaddr *) &sAddr, slen)==-1){
            die("sendto()");
        }
    }
    
 
    close(s);
    return 0;
}
