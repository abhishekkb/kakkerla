#include "Balrog.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

Balrog::Balrog() :Demon(){
    //ctor
}

Balrog::~Balrog(){
    //dtor
}

Balrog::Balrog(int newStrength, int newHitpoints):Demon(newStrength , newHitpoints){
    //ctor
}

string Balrog::getSpecies(){
    return "Balrog";
}

int Balrog::getDamage(){


    /*
    int damage = (rand()%getStrength())+1;

    // Demons can inflict damage of 50 with a 5% chance
    if ((rand( ) % 100) < 5){
        damage = damage + 50;
        cout << "Demonic attack inflicts 50 "
        << " additional damage points!" << endl;
    }
    */

    int damage = Demon::getDamage();

    // Balrogs are so fast they get to attack twice
    int damage2 = (rand() % getStrength()) + 1;
    cout << "Balrog speed attack inflicts " << damage2 <<
            " additional damage points!" << endl;
    damage = damage + damage2;

    cout << "Balrog caused " << damage << " points of damange" << endl;
    return damage;
}
