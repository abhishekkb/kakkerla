#include <iostream>
#include "Balrog.h"
#include "Creature.h"
#include "CyberDemon.h"
#include "Demon.h"
#include "Elf.h"
#include "Human.h"

using namespace std;
void battle(Creature &c1, Creature &c2);
int main(){


    //battle 1
    Demon demon1(500, 1000);
    Elf elf1(700,800);
    battle((Creature &) demon1, (Creature &) elf1);

    //battle 2
    Elf elf2(800, 1000);
    Balrog balrog1(1000,800);
    battle((Creature &) elf2, (Creature &) balrog1);

    //battle 3
    Human human1(800, 1000);
    CyberDemon cyberDemon1(1000,800);
    battle((Creature &) human1, (Creature &) cyberDemon1);

    //battle 4
    Human human2(900, 800);
    Demon demon2(1000,600);
    battle((Creature &) human2, (Creature &) demon2);

    return 0;
}

void battle(Creature &c1, Creature &c2){

    cout << "------- " << "battle between a " << c1.getSpecies()
         << " and a " << c2.getSpecies() << " -------"<<  endl;

    do{
        //c2 attacks c1 first
        c1.setHitpoints(c2.battleArena(c2, c1));
        cout << c1.getSpecies() << " got attacked by " << c2.getSpecies()
             << " and left with " << c1.getHitpoints() << " points" << endl;

        /*
        //if with the previous attack, the other creature hitpoints goes below zero then break the loop
        if(c2.getHitpoints()< 0 || c1.getHitpoints() < 0){
            break;
        }
        */

        //Now let's make c1 attack the c2
        c2.setHitpoints(c1.battleArena(c1, c2));
        cout << c2.getSpecies() << " got attacked by" << c1.getSpecies()
             << " and left with " << c2.getHitpoints() << " points" << endl;
    }while(c2.getHitpoints()< 0 && c1.getHitpoints() < 0);

    if(c2.getHitpoints()< 0 && c1.getHitpoints() < 0){
        cout << "Match tied" << endl;
    }else if(c2.getHitpoints() > c1.getHitpoints()){
        cout << c2.getSpecies() << " has won" << endl;
    }else{
        cout << c1.getSpecies() << " has won" << endl;
    }

    cout << endl;
}
