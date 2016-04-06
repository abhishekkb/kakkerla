#ifndef CREATURE_H
#define CREATURE_H

#include<string>
using namespace std;

class Creature{
    private:
        //int type; // 0 human, 1 cyberdemon, 2 balrog, 3 elf
        int strength; // How much damage we can inflict
        int hitpoints; // How much damage we can sustain
        string getSpecies(); // Returns type of species
    public:
        // Initialize to human, 10 strength, 10 hit points
        Creature();

        // Initialize creature to new type, strength, hit points
        Creature(/*int newType,*/ int newStrength, int newHit);
        virtual ~Creature();
        // Returns amount of damage this creature
        // inflicts in one round of combat
        virtual int getDamage();


    public:
        // adding appropriate accessor and mutator functions
        // for type, strength, and hit points

        //int getType();
        int getStrength();
        int getHitpoints();

        //void setType(int newType);
        void setStrength(int newStrength);
        void setHitpoints(int newHitpoints);

};

#endif // CREATURE_H
