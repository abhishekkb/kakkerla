#include "rsocket.h"

#define PORT 6000

int main(){
	//printf("hello world\n");
	
	clear();
	int rsockfd = r_socket(PF_INET, SOCK_BRP, 0);
	// printf("in main = %d\n", rsockfd);

	if(rsockfd == -1){
		exit(0);
	}
	
	struct sockaddr_in addr;
	
	//zero out the structure
	memset((char *) &addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(PORT);
	addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	memset(addr.sin_zero, '\0', sizeof addr.sin_zero);
	
	if((r_bind(rsockfd , (struct sockaddr*)&addr, sizeof(addr) )) == -1){
		r_close(rsockfd);//??
		exit(0);
	}
	
	char buf[10];
	struct sockaddr cAddr;
	socklen_t addrlen;
	while(true){
		r_recvfrom(rsockfd, buf,5,0,(struct sockaddr *)&cAddr, &addrlen);
		//printf("in main = %c \n",buf[0]);
		printf("%s",buf);
	}
	
	return 0;
	//while(true){}
}
