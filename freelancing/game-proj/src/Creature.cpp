#include "Creature.h"
#include "CreatureStats.h"
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;


Creature::Creature(){
    //type = HUMAN;
    strength = HUMAN_INIT_STRENGTH;
    hitpoints = HUMAN_INIT_HITPOINTS;
}

Creature::Creature(/*int newType,*/ int newStrength, int newHit){
    //type = newType;
    strength = newStrength;
    hitpoints = newHit;
}
Creature::~Creature(){
    //dtor
}
string Creature::getSpecies(){
    /*
    switch (type){
        case 0: return "Human";
        case 1: return "Cyberdemon";
        case 2: return "Balrog";
        case 3: return "Elf";
    }
    return "Unknown";
    */
    return "creature";
}

int Creature::getDamage( ){

    int damage;
    // All creatures inflict damage, which is -
    // - a random number up to their strength
    damage = (rand( ) % strength) + 1;
    cout << getSpecies( ) << " attacks for "
         << damage << " points!" << endl;

    return damage;

    /*
    int damage;
    // All creatures inflict damage, which is a
    // random number up to their strength
    damage = (rand( ) % strength) + 1;
    cout << getSpecies( ) << " attacks for " <<
    damage << " points!" << endl;

    // Demons can inflict damage of 50 with a 5% chance
    if ((type = 2) || (type == 1)){
        if ((rand( ) % 100) < 5){
            damage = damage + 50;
            cout << "Demonic attack inflicts 50 "
            << " additional damage points!" << endl;
        }
    }

    // Elves inflict double magical damage with a 10% chance
    if (type == 3){
        if ((rand( ) % 10)==0){
            cout << "Magical attack inflicts " << damage <<
            " additional damage points!" << endl;
            damage = damage * 2;
        }
    }

    // Balrogs are so fast they get to attack twice
    if (type == 2){
        int damage2 = (rand() % strength) + 1;
        cout << "Balrog speed attack inflicts " << damage2 <<
        " additional damage points!" << endl;
        damage = damage + damage2;
    }
    return damage;
    */
}

int Creature::getStrength(){
    return strength;
}

void Creature::setStrength(int newStrength){
    strength = newStrength;
}

int Creature::getHitpoints(){
    return hitpoints;
}

void Creature::setHitpoints(int newHitpoints){
    hitpoints = newHitpoints;
}


