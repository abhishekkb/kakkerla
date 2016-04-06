#ifndef RSOCKET_H_INCLUDED // to avoid multiple defintions
#define RSOCKET_H_INCLUDED

#include<stdio.h> //printf
#include<string.h> //memset
#include<stdlib.h> 
#include<arpa/inet.h>
#include<sys/socket.h>
#include<fcntl.h> // for open
#include<unistd.h> // for close
#include<pthread.h> // for threads
#include<signal.h> // for using signals
#include<sys/time.h>


#define SOCK_BRP -100
#define T 1000000 // timeout period in microseconds
#define SLEEPTIME 1000 // sleeptime for r_recvfrom if there is no data in recv_table
#define BUFLEN 20
#define BRP_MAX 25
#define P 0

#define clear() printf("\033[H\033[J")

enum booleanEnum {
	true=1,
	false=0
};

typedef enum booleanEnum bool;


// following function declarations are taken from man pages of socket, recvfrom, sendto and close 
// and the function names are changed to r_socket, r_recvfrom, r_sendto and r_close
int r_socket(int domain, int type, int protocol);
int r_bind(int sockfd, const struct sockaddr *addr, socklen_t addrlen);
ssize_t r_sendto(int sockfd, char *buf, size_t len, int flags, const struct sockaddr *dest_addr, socklen_t addrlen);
ssize_t r_recvfrom(int sockfd, char *buf, size_t len, int flags, struct sockaddr *src_addr, socklen_t *addrlen);
int r_close(int fd);

//thread functions
void* thread_R(void *);
void* thread_S(void *);

// signal handlers
void sig_handler(int );

// drop message function
bool dropMessage(float p);

// function to calculate time difference
long timeval_diff(struct timeval *difference, struct timeval *end_time, struct timeval *start_time);

//--------------------------------------------------------------------//
enum msgType {
	dat='1',
	ack='0'
};
//table defintions
struct unack_table{
	char data;
	int seq;
	struct sockaddr sendToaddr;
	struct timeval sentTime; //timestamp
	struct unack_table *next;
};
struct recv_table{
	enum msgType pt;
	struct sockaddr fromAddr;
	char data;
	int seq;
	struct recv_table *next;
};
//--------------------------------------------------------------------//
struct rsockInfo{
	// threads
	pthread_t R;
	pthread_t S;
	//socket info
	int sockfd;
	struct sockaddr sAddr;
	// mutexes
	pthread_mutex_t mutex_unack;
	pthread_mutex_t mutex_recv;
	// tables
	struct unack_table *head_ut;
	struct recv_table *head_rt;
	struct conn *next;
};
typedef struct rsockInfo rsi;
//--------------------------------------------------------------------//

#endif // RSOCKET_H_INCLUDED
