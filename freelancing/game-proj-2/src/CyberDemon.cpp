#include "CyberDemon.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

CyberDemon::CyberDemon():Demon(){
    //ctor
}

CyberDemon::~CyberDemon(){
    //dtor
}
CyberDemon::CyberDemon(int newStrength, int newHitpoints):Demon(newStrength , newHitpoints){
    //ctor
}

string CyberDemon::getSpecies(){
    return "CyberDemon";
}
