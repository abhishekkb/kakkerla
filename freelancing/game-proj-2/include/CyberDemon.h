#ifndef CYBERDEMON_H
#define CYBERDEMON_H

#include<string>
#include "Demon.h"

using namespace std;


class CyberDemon : public Demon{
    public:
        CyberDemon();
        virtual ~CyberDemon();
        CyberDemon(int newStrength, int newHitpoints);
        string getSpecies();
    protected:

    private:
};

#endif // CYBERDEMON_H
