all:
	gcc -c rsocket.c
	ar cr librsocket.a rsocket.o
	gcc usr1.c librsocket.a -o usr1.o -lpthread
	gcc usr2.c librsocket.a -o usr2.o -lpthread
