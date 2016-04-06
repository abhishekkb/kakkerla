#ifndef BALROG_H
#define BALROG_H

#include<string>
#include "Demon.h"


class Balrog : public Demon
{
    public:
        Balrog();
        virtual ~Balrog();

        Balrog(int newStrength, int newHitpoints);

        int getDamage();
        string getSpecies();
    protected:

    private:
};

#endif // BALROG_H
