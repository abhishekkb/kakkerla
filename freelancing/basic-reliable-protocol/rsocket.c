#include "rsocket.h"

// global variables
int fd;

pthread_mutex_t mutex_unack = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_recv = PTHREAD_MUTEX_INITIALIZER;

struct unack_table *head_ut = NULL;
struct recv_table *head_rt = NULL;

pthread_t R, S;

int brpCount = 0;
int sqn_num=0;


int r_socket(int domain, int type, int protocol){
	
	signal(SIGINT, sig_handler);
	 
	int sockfd;
	
	// check if maximum number of socket creations has reached
	if(brpCount == BRP_MAX){
		perror("BRP socket limit reached");
		return -1;
	}
	if(type == SOCK_BRP){
		//create a UDP socket
		sockfd = socket(domain, SOCK_DGRAM, protocol);
		if( sockfd == -1){
			perror("unable to create socket");
			return -1;
		}
		//printf("in r_socket sockfd = %d\n",sockfd);
	}else{
		perror("user trying to create socket type which is not of type SOCK_BRP");
		return -1;
	}
	
	// create thread for R and S
	pthread_create(&R, NULL, thread_R, (void *) &sockfd);
	pthread_create(&S, NULL, thread_S, (void *) &sockfd);

	pthread_detach(R);
	pthread_detach(S);
	
	fd = sockfd;
	printf("in r_socket = %d\n", fd);

	return sockfd; // success
}
//---------------------------------------------------------------------/
int r_bind(int sockfd, const struct sockaddr *addr, socklen_t addrlen){

	//bind socket to addr-port
	if( bind(sockfd , addr, addrlen ) == -1){
		perror("unable to bind the socket");
		return -1;
	}
	//printf("binding successful, sockfd= %d\n", sockfd);
	return 1; // success
}
//---------------------------------------------------------------------/
ssize_t r_sendto(int sockfd, char *buf, size_t len, int flags, const struct sockaddr *dest_addr, socklen_t addrlen){
	
	struct unack_table *new_ut;
	struct unack_table *current;

	
	// send msg immediately using sendto along with sqn number in msg
	//char *buff = (void*)buf;
	int i = 0;
	while(buf[i] != '\0'){
		char msg[BUFLEN];
		// adding sequence number to the message
		sprintf(msg,"%c%c;%d",dat,buf[i],++sqn_num);// message is of type dat
		if (sendto(sockfd, (const void*) msg, sizeof(msg) , 0 , (struct sockaddr *) dest_addr, addrlen)==-1){
			perror("sendto()");
			return -1;
		}
		//printf("sending %s\n",msg);
		new_ut = (struct unack_table*) malloc(sizeof(struct unack_table));
		new_ut->data=buf[i];
		new_ut->seq=sqn_num;
		//printf("unack-data : %c, %d, \n", temp->data, temp->seq);
		new_ut->sendToaddr = *dest_addr;
		if(gettimeofday(&(new_ut->sentTime),NULL)){
			perror("gettimeofday()");
			return -1;
		}
		new_ut->next=NULL;
		
		//printf("hello before mutex\n");
		// add temp structure to unack-table at the last node
		// mutex lock for unack
		struct unack_table *temp;
		//pthread_mutex_lock(&mutex_unack);
			if(head_ut == NULL){
				head_ut = new_ut;
				//printf("head is null\n");
				//current = new_ut;
			}else{
				temp = head_ut;
				while(temp->next!=NULL){
					temp = temp->next;
					//printf("hello in mutex\n");
				}
				temp->next = new_ut;
			}
		// mutex unlock for unack
		//pthread_mutex_unlock(&mutex_unack);
		
		i++;
		usleep(100000); //sleep for 0.1 sec
	}

	//printf("sendto ends\n");
	
	/*
	struct unack_table *temp;
	temp = head_ut;
	//printf("head-data : %c, %d, \n", head_ut->data, head_ut->seq);
	do{
		printf("unack-data : %c, %d, \n", temp->data, temp->seq);
		temp = temp->next;
	}while(temp!=NULL);
	*/
}
//--------------------------------------------------------------------//
ssize_t r_recvfrom(int sockfd, char *buf, size_t len, int flags, struct sockaddr *src_addr, socklen_t *addrlen){
	
	while(true){
		if(head_rt == NULL){
			usleep(SLEEPTIME);
		}else{
			//read the recv_table
			//pthread_mutex_lock(&mutex_recv);
				struct recv_table *rt = head_rt;
				char data[2] = {rt->data,'\0'};
				strcpy(buf, data);
				//printf("head-data = %s\n",buf);
				if(head_rt->next != NULL){
					head_rt = head_rt->next;
					free(head_rt);
				}else{
					head_rt = NULL;
				}
			//pthread_mutex_unlock(&mutex_recv);
			break;
		}
	}
}
//--------------------------------------------------------------------//
void* thread_R(void *sockfd){
	
	// to create random numbers, we need to seed the rand algo with time
	srand ( time(NULL) ); //srand(getpid())
	
	//printf("Thread R created\n");
	
	//convert sockfd to its datatype
	int sock = fd;
	//printf("in thread_R = %d\n", sock);
	
	struct recv_table *new_rt;
	struct recv_table *current;
	
	struct sockaddr_in cAddr;
     
    int i, slen = sizeof(cAddr) , recv_len;
    char buf[BUFLEN];
	
	while(true){
		//printf("Waiting for data...\n");
		fflush(stdout);
		 
		//receive data -- blocking call
		if ((recv_len = recvfrom(fd, buf, BUFLEN, 0, (struct sockaddr *) &cAddr, &slen)) == -1){
			perror("recvfrom()");
			continue;
		}
		
		//printing details of the sender and the data received
		//printf("Received packet from %s:%d\n", inet_ntoa(cAddr.sin_addr), ntohs(cAddr.sin_port));
		//printf("rcvd Data: %s\n" , buf);

		//fflush(stdout);
		bool p = dropMessage(P);
		
		if(buf[0] == dat && !p){ // "!dropMessage(P)" - not dropMessage()
		// if msg is data and not dropped
			// store in received  msg table
			new_rt = (struct recv_table*) malloc(sizeof(struct recv_table));

			//populating new_rt structure
			char type, data, delim; int seq;
			sscanf(buf,"%c%c%c%d", &type, &data, &delim, &seq);
			new_rt->pt = type;
			new_rt->fromAddr = *((struct sockaddr *) (&cAddr));
			new_rt->data = data;
			new_rt->seq = seq;
			new_rt->next = NULL;
			//printf("rcvd-data : %c, %c, %d, \n", new_rt->pt, new_rt->data, new_rt->seq);
			
			struct recv_table *temp;
			
			// add temp structure to recv-table at the last node
			// mutex lock for recv
			//pthread_mutex_lock(&mutex_recv);
				if(head_rt == NULL){
					head_rt = new_rt;
					//printf("head is null\n");
					//current = new_rt;
				}else{
					temp = head_rt;
					while(temp->next!=NULL){
						temp = temp->next;
					}
					temp->next = new_rt;
					//printf("rcvd-data in node: %c, %c, %d, \n", temp->next->pt, temp->next->data, temp->next->seq);
				}
			// mutex unlock for recv
			//pthread_mutex_unlock(&mutex_recv);
			
			// send an ack to the sender of this message
			//drop ack or not drop
			if(!p){
				char msg[10];
				sprintf(msg,"%c%c;%d",ack,'-',seq);// message is of type dat
				sendto(fd, (const void*) msg, sizeof(msg) , 0 , (struct sockaddr *) (&cAddr), sizeof(cAddr));
			}
			
			
		}else if(buf[0] == ack){
		// if msg is an ack
			// remove corresponding data from unacked table
				// check for msg sqn for which ack is sent
				// delete corresponding data
			char type, data, delim; int seq;
			sscanf(buf,"%c%c%c%d", &type, &data, &delim, &seq);

			
			// mutex lock for unack
			//pthread_mutex_lock(&mutex_unack);
				if(head_ut != NULL){
					struct unack_table *curr = head_ut, *prev,*temp;
					if(head_ut->next==NULL && head_ut->seq == seq){
						curr = head_ut;
						head_ut = NULL;
						free(curr);
					}else{
						if(head_ut->seq == seq){
							//first node
							curr = head_ut;
							head_ut = head_ut->next;
							free(curr);
						}else{
							curr = head_ut;
							prev = head_ut;
							while(curr->next != NULL){
								if(curr->seq == seq){
									break;
								}
								prev = curr;
								curr = curr->next;
							}
							prev->next = (curr->next == NULL)? NULL : curr->next;
							free(curr);	
						}
					}
				}
			//mutex unlock for recv
			//pthread_mutex_unlock(&mutex_unack);
			
		}
		
	}
}

//--------------------------------------------------------------------//
void* thread_S(void *sockfd){
	
	//printf("Thread S created\n");
	//convert sockfd to its datatype
	
	while(true){
		//sleep for time T
		usleep(T); // sleep for 1000000 micro seconds = 1 second
		
		
		// mutex lock for unack
		//pthread_mutex_lock(&mutex_unack);
			if(head_ut == NULL){
				continue;
			}
			struct unack_table *curr, *prev, *last;
			prev = curr = last = head_ut;
			while(last->next != NULL ){
				last = last->next;
			}
			//check for messages in unack-msg-table
			// for each msg in unack-msg-table
			do{
				// for timeout periods 2T
				struct timeval currTime;
				gettimeofday(&currTime,NULL);
				long diff = timeval_diff(NULL,&currTime,&(curr->sentTime));
				if (diff > 2*T){
				// if timeout > 2T 
					// retransmit
					char msg[10];
					sprintf(msg,"%c%c;%d",dat,curr->data,curr->seq);//++sqn_num);
					//curr->seq = sqn_num;
					curr->sentTime = currTime;
					struct sockaddr_in *sss = (struct sockaddr_in*) (&(curr->sendToaddr));
					sendto(fd, (const void*) msg, sizeof(msg) , 0 , (struct sockaddr *) sss, sizeof(*sss));
					//printf("sending %s\n", msg);
					//printf("time diff found %ld >< timeout = %ld\n",diff, (long) 2*T);
					curr = curr->next;
				}
			}while(curr != NULL);
		// mutex unlock for unack
		//pthread_mutex_unlock(&mutex_unack);
		
		//break;
	}
	
	
}

int r_close(int fd){
	
	close(fd);
	printf ("socket closed\n");
	
	//reducing socket count
	brpCount--;
	
	//freeing unack_table
	{
		struct unack_table *current = head_ut;
		struct unack_table *next;
		while(current!=NULL){
			next=current->next;
			//printf("unack-data : %c, %d, \n", current->data, current->seq);
			//struct sockaddr_in *sss = (struct sockaddr_in*) (&(current->sendToaddr));
			//printf("       from : %s:%d\n\n", inet_ntoa(sss->sin_addr), ntohs(sss->sin_port));
			free(current);
			current=next;
		}
	}
	//freeing recv_table
	{
		struct recv_table *current = head_rt;
		struct recv_table *next;
		while(current!=NULL){
			next=current->next;
			//printf("rcvd-data : %c, %c, %d, \n", current->pt, current->data, current->seq);
			//struct sockaddr_in *sss = (struct sockaddr_in*) (&(current->fromAddr));
			//printf("       from : %s:%d\n\n", inet_ntoa(sss->sin_addr), ntohs(sss->sin_port));
			free(current);
			current=next;
		}
	}
	//destroy mutexes
	pthread_mutex_destroy(&mutex_unack);
	pthread_mutex_destroy(&mutex_recv);
	
	//stop threads
	//pthread_join(R,NULL);//????
}
//--------------------------------------------------------------------//
void sig_handler(int signo){
	if (signo == SIGINT){
		// if ctrl+c is pressed
		printf("\nsignal \"SIGINT\" received (ctrl+c)\n");
		r_close(fd);
		exit(0);
	}
}
//--------------------------------------------------------------------//
bool dropMessage(float p){
	float random = (float)((rand())%(100)/((float)100));;
	/*
	if(random<p)
		printf("dropping message\n");
	else 
		printf("not dropping message\n");
	*/
	printf("\nrand=%f\n",random);
	return random<p;//?true:false;
}

//--------------------------------------------------------------------//
long timeval_diff(struct timeval *difference, struct timeval *end_time, struct timeval *start_time){
	struct timeval temp_diff;

	if(difference==NULL){
		difference=&temp_diff;
	}

	difference->tv_sec  = end_time->tv_sec -start_time->tv_sec ;
	difference->tv_usec = end_time->tv_usec-start_time->tv_usec;

	/* Using while instead of if below makes the code slightly more robust. */

	while(difference->tv_usec<0){
		difference->tv_usec+=1000000;
		difference->tv_sec -=1;
	}

	return 1000000L*difference->tv_sec+difference->tv_usec;
}
//--------------------------------------------------------------------//
