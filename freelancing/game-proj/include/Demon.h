#ifndef DEMON_H
#define DEMON_H

#include<string>
#include "Creature.h"


class Demon : public Creature
{
    public:
        Demon();
        Demon(int newStrength, int newHitpoints);

        virtual ~Demon();

        virtual int getDamage();
        string getSpecies();

    protected:

    private:
};

#endif // DEMON_H
