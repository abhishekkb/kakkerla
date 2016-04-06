#include "BinNode.h"
#include "BST.h"


int main(){
    BST treeOfNums;

    treeOfNums.insert(40);
    treeOfNums.insert(20);
    treeOfNums.insert(80);
    treeOfNums.insert(19);
    treeOfNums.insert(28);
    treeOfNums.insert(62);
    treeOfNums.insert(100);
    treeOfNums.insert(23);
    treeOfNums.insert(58);
    treeOfNums.insert(69);
    treeOfNums.insert(90);
    treeOfNums.insert(109);
    treeOfNums.insert(25);
    treeOfNums.insert(88);
    treeOfNums.insert(96);
    treeOfNums.insert(106);
    treeOfNums.insert(111);
    treeOfNums.insert(93);
    treeOfNums.insert(99);

	//inorder printing using iterative traversal
    treeOfNums.print();

    treeOfNums.find(82);
    treeOfNums.find(58);

    treeOfNums.remove(100);

    treeOfNums.print();

    treeOfNums.find(100);
    treeOfNums.find(106);


    return 0;
}
