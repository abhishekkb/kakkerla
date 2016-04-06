#ifndef BINNODE_H_
#define BINNODE_H_

#include <cstddef>

class BinNode {
    private:
	int m_element;
	BinNode *m_left, *m_right;

    public:
	BinNode()
	: m_element(-1), m_left(NULL), m_right(NULL)
	{};
	BinNode(int el)
	: m_element(el), m_left(NULL), m_right(NULL)
	{};

	int element();
	int setElement(int el);

	BinNode* left();
	void setLeft(BinNode *left);

	BinNode* right();
	void setRight(BinNode *right);

	bool isLeaf();

};


#endif
