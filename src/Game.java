import java.util.Random;

class Player {
    int health;

    Player(int health) {
        this.health = health;
    }

    boolean isAlive() {
        return health > 0;
    }
}

class Medic extends Player {
    int healAmount;

    Medic(int health, int healAmount) {
        super(health);
        this.healAmount = healAmount;
    }

    void heal(Player[] team) {
        for (Player player : team) {
            if (player != this && player.health < 100 && player.isAlive()) {
                player.health = Math.min(100, player.health + healAmount);
                System.out.println("Medic healed a player to " + player.health + " health.");
                break;
            }
        }
    }

    void takeDamage(int damage) {
        health -= damage;
        System.out.println("Medic took " + damage + " damage, remaining health: " + health);
    }
}

class Golem extends Player {
    Golem(int health) {
        super(health);
    }

    void takeDamage(int damage) {
        // Golem принимает 1/5 урона от босса по другим игрокам
        int absorbedDamage = damage / 5;
        health -= absorbedDamage;
        System.out.println("Golem absorbed " + absorbedDamage + " damage, remaining health: " + health);
    }
}

class Lucky extends Player {
    Lucky(int health) {
        super(health);
    }

    boolean evadeAttack() {
        Random random = new Random();
        return random.nextBoolean(); // Шанс уклонения от удара
    }
}

class Witcher extends Player {
    Witcher(int health) {
        super(health);
    }

    void revive(Player[] team) {
        for (Player player : team) {
            if (!player.isAlive()) {
                player.health = 100; // Воскрешение
                System.out.println("Witcher revived a player with 100 health.");
                this.health = 0; // Witcher погибает
                break;
            }
        }
    }
}

class Thor extends Player {
    Thor(int health) {
        super(health);
    }

    boolean stunBoss() {
        Random random = new Random();
        return random.nextBoolean(); // Шанс оглушить босса
    }
}

class Boss {
    void attack(Player player, int damage) {
        if (player.isAlive()) {
            player.health -= damage;
            System.out.println("Boss attacked a player for " + damage + " damage.");
        }
    }
}

public class Game {
    public static void main(String[] args) {
        int healAmount = 20; // Уровень лечения медика
        Player medic = new Medic(100, healAmount);
        Player golem = new Golem(200);
        Player lucky = new Lucky(100);
        Player witcher = new Witcher(80);
        Player thor = new Thor(100);

        Player[] team = {medic, golem, lucky, witcher, thor};
        Boss boss = new Boss();

        // Пример игрового процесса
        for (int round = 1; round <= 3; round++) {
            System.out.println("Round " + round);

            // Босс атакует каждого игрока, кроме Lucky, если он уклоняется
            for (Player player : team) {
                if (player.isAlive()) {
                    if (player instanceof Lucky && ((Lucky) player).evadeAttack()) {
                        System.out.println("Lucky evaded the attack!");
                    } else {
                        boss.attack(player, 30);
                    }
                }
            }

            // Golem принимает часть урона
            if (golem.isAlive()) {
                ((Golem) golem).takeDamage(30); // Пример урона для Golem
            }

            // Medic лечит
            if (medic.isAlive()) {
                ((Medic) medic).heal(team);
            }

            // Witcher может воскрешать
            if (witcher.isAlive()) {
                ((Witcher) witcher).revive(team);
            }

            // Thor пытается оглушить босса
            if (thor.isAlive() && ((Thor) thor).stunBoss()) {
                System.out.println("Thor stunned the boss for this round! Boss skips attack.");
                continue; // Босс пропускает атаку
            }

            System.out.println();
        }
    }
}

