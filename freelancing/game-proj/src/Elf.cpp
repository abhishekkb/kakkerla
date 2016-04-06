#include "Elf.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

Elf::Elf(){
    //ctor
}

Elf::~Elf(){
    //dtor
}

Elf::Elf(int newStrength , int newHitpoints):Creature(newStrength , newHitpoints){
    //ctor
}

int Elf::getDamage(){

    int damage = (rand()%getStrength())+1;

    // Elves inflict double magical damage with a 10% chance

    if ((rand( ) % 10)==0){
        cout << "Magical attack inflicts " << damage <<
        " additional damage points!" << endl;
        damage = damage * 2;
    }
    cout << "attack caused " << damage << " points by the demon"<< endl;

    return damage;
}

string Elf::getSpecies(){
    return "Elf";
}
