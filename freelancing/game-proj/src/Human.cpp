#include "Human.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

Human::Human():Creature(){
    //ctor
}

Human::Human(int newStrength, int newHitpoints):Creature(newStrength , newHitpoints){
    //ctor
}

Human::~Human(){
    //dtor
}

string Human::getSpecies(){
    return "Human";
}



