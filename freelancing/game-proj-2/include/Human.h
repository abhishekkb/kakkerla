#ifndef HUMAN_H
#define HUMAN_H

#include<string>
#include "Creature.h"


class Human : public Creature
{
    public:
        Human();
        Human(int newStrength, int newHitpoints);
        virtual ~Human();

        string getSpecies();;

    protected:

    private:
};

#endif // HUMAN_H
