#include "Demon.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

Demon::Demon(): Creature(){
    //ctor
}

Demon::~Demon(){
    //dtor
}

Demon::Demon(int newStrength , int newHitpoints):Creature(newStrength , newHitpoints){
    //ctor
}

int Demon::getDamage(){

    int damage = (rand()%getStrength())+1;

    // Demons can inflict damage of 50 with a 5% chance
    if ((rand( ) % 100) < 5){
        damage = damage + 50;
        cout << "Demonic attack inflicts 50 "
        << " additional damage points!" << endl;
    }

    cout << "attack caused " << damage << " points by the elf"<< endl;

    return damage;
}

string Demon::getSpecies(){
    return "Demon";
}
