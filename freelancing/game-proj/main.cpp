#include <iostream>
#include "Balrog.h"
#include "Creature.h"
#include "CyberDemon.h"
#include "Demon.h"
#include "Elf.h"
#include "Human.h"

using namespace std;

int main(){

    Demon demon(30, 80);
    Balrog balrog(20, 60);
    Elf elf(40,90);
    CyberDemon cyberDemon(20,70);
    Human human(10,60);

    cout << demon.getSpecies() << " ..." << endl;
    demon.getDamage();

    cout << balrog.getSpecies() << " ..." << endl;
    balrog.getDamage();

    cout << elf.getSpecies() << " ..." << endl;
    elf.getDamage();

    cout << cyberDemon.getSpecies() << " ..." << endl;
    cyberDemon.getDamage();

    cout << human.getSpecies() << " ..." << endl;
    human.getDamage();


    return 0;
}
