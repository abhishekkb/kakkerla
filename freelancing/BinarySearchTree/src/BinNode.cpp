#include "BinNode.h"


int BinNode::element(){
    return m_element;
}

int BinNode::setElement(int el){
    m_element = el;
    return m_element;
}

BinNode* BinNode::left(){
    return m_left;
}

void BinNode::setLeft(BinNode *left){
    m_left = left;
}

BinNode* BinNode::right(){
    return m_right;
}

void BinNode::setRight(BinNode *right){
    m_right = right;
}

bool BinNode::isLeaf(){
    return (m_right == NULL && m_left == NULL);
}



