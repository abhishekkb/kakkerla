#ifndef BST_H_
#define BST_H_
#include "BinNode.h"
#include <iostream>
#include <cstddef>

class BST {
    private:
	BinNode *root;
	void recPrint(BinNode *node);
	//void recPrint(BinNode *node,Stack* stack);
    void stackPrint();

    public:
	BST()
        : root(NULL)
        {};

	void insert(int element);

	bool remove(int element);
	bool find(int element);

	void print();

	bool empty();
};

#endif
