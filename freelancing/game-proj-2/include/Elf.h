#ifndef ELF_H
#define ELF_H

#include<string>
#include "Creature.h"


class Elf : public Creature
{
    public:
        Elf();
        virtual ~Elf();
        Elf(int newStrength, int newHitpoints);
        int getDamage();
        string getSpecies();
    protected:

    private:
};

#endif // ELF_H
