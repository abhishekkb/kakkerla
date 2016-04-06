#include "BST.h"
#include<stack>

using namespace std;

void BST::insert(int element){
    BinNode *newNode = new BinNode(element);
    BinNode *parent = NULL;

    if(empty()){
	root = newNode;
	return;
    }

    BinNode *curr = root;

    while(curr) {
	parent = curr;

	if(newNode->element() > curr->element()){
	    curr = curr->right();
        }
	else if(newNode->element() == curr->element()){
        delete newNode;
	    return;
    }
	else
	    curr = curr->left();
    }

    if(newNode->element() < parent->element())
	parent->setLeft(newNode);
    else
	parent->setRight(newNode);
}


bool BST::remove(int element){
    if(empty())
	return false;

    BinNode *parent = NULL;
    BinNode *curr = root;
    bool parentLeft = true;

    while(curr) {
	if(element == curr->element()){
	    cout << "Found element " << element << "!" << endl;

	    if(curr->isLeaf()){
		if(curr == root){
		    delete root;
		    root = NULL;
		}
		if(parentLeft){
		    delete curr;
		    parent->setLeft(NULL);
		} else {
		    delete curr;
		    parent->setRight(NULL);
		}
		return true;
	    }


	    if(curr->right() == NULL){
		if(curr == root){
		    root = curr->left();
		    delete curr;
		    return true;
		}
		if(parentLeft){
		    parent->setLeft(curr->left());
		    delete curr;
		} else {
		    parent->setRight(curr->left());
		    delete curr;
		}
	    } else if (curr->left() == NULL) {
		if(curr == root){
		    root = curr->right();
		    delete curr;
		    return true;
		}
		if(parentLeft){
		    parent->setLeft(curr->right());
		    delete curr;
		} else {
		    parent->setRight(curr->right());
		    delete curr;
		}
	    }

	    if (curr->left() != NULL && curr->right() != NULL){
		BinNode *right_child = curr->right();

		if(right_child->isLeaf()){
		    curr->setElement(right_child->element());
		    delete right_child;
		    curr->setRight(NULL);
		} else {
		    if((curr->left())->right() != NULL){
			BinNode *left_curr;
			BinNode *left_curr_parent;
			left_curr_parent = curr->right();
			left_curr = (curr->right())->left();
			while(left_curr->left() != NULL) {
			    left_curr_parent = left_curr;
			    left_curr = left_curr->left();
			}
			curr->setElement(left_curr->element());
			delete left_curr;
			left_curr_parent->setLeft(NULL);
		    } else {
			BinNode *right_curr = curr->right();
			curr->setElement(right_curr->element());
			curr->setRight(right_curr->right());
			delete right_curr;
		    }

		}
	    }

	    return true;
	}
	else if(element > curr->element()){
	    parent = curr;
	    parentLeft = false;
	    curr = curr->right();
	}
	else{
	    parent = curr;
	    parentLeft = true;
	    curr = curr->left();
	}
    }

    cout << "Remove returns False. Element " << element << " not found!" << endl;
    return false;
}



bool BST::find(int element){
    if(empty())
	return false;

    BinNode *parent = NULL;
    BinNode *curr = root;

    while(curr) {
	parent = curr;

	if(element > curr->element())
	    curr = curr->right();
	else if(element == curr->element()){
	    cout << "Found element " << element << "!" << endl;
	    return true;
	}
	else
	    curr = curr->left();
    }
    cout << "Element " << element << " not found!" << endl;
    return false;
}


void BST::print(){
    //recPrint(root);
    stackPrint();
}

void BST::stackPrint(){
    /*
    Stack *stack = new Stack();
    recPrint(root,stack);
    while(!stack->empty()){
        cout << stack->pop() << endl;
    }
    */

    stack<BinNode*> s;
    BinNode *current = root;
    bool isDone = false;
    while (!isDone) {
        if (current) {
            s.push(current);
            current = current->left();
        } else {
            if (s.empty()) {
                isDone = true;
            } else {
                current = s.top();
                s.pop();
                cout << current->element() << " ";
                current = current->right();
            }
        }
    }
    cout << endl;

}
/*
void BST::recPrint(BinNode *node,Stack* stack){
    if(node != NULL){
        recPrint(node->left());
        //cout << node->element() << endl;
        stack->push(node->element());
        recPrint(node->right());
    }
}
*/
void BST::recPrint(BinNode *node){
    if(node != NULL){
	recPrint(node->left());
	cout << node->element() << endl;
	recPrint(node->right());
    }
}



bool BST::empty(){
    return (root == NULL);
}
