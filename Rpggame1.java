package rpg; //made by 20233132 김민수

import java.util.Scanner;

public class Rpggame1 {
	static int hero_level = 1, hero_power = 10, hero_hp = 100, hero_defense = 5, hero_mp = 50, hero_experience = 0,
			hero_money = 100;
	static int monster_hp = 0, monster_defense, monster_power, monster_mp, monster_level, monster_experience,
			monster_money;
	static String hero_name, monster_name;
	static int original_hero_hp = hero_hp;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.print("유저의 이름을 입력하세요: ");
		hero_name = in.nextLine();

		System.out.println("게임에 입장하였습니다.");

		while (true) {
			printHeroInfo();
			System.out.println("1. 사냥터에 입장");
			System.out.println("2. 포션 상점에 입장");
			System.out.println("3. 종료");
			System.out.print("입장할 장소를 선택하세요 (1-3): ");
			int choice = Integer.parseInt(in.nextLine()); // parseInt:string을 Integer 타입으로 변환

			switch (choice) {
			case 1:
				enterHuntingGround();
				selectMonster(in);
				mainBattleLoop(in);
				break;

			case 2:
				potionStore(in);
				break;

			case 3:
				System.out.println("게임을 종료합니다.");
				return;

			default:
				System.out.println("1~3번중에서 입력하세요."); // 다른 번호를 눌렸을 때
				break;
			}
			printHeroInfo(); // 수정된 부분: 히어로 정보를 출력
		}
	}

	static void enterHuntingGround() {
		System.out.println(hero_name + "님, 사냥터에 입장합니다!");
	}

	static void selectMonster(Scanner in) {
		System.out.println("사냥할 몬스터를 선택하세요: ");
		System.out.println("1. 너구리 (레벨 1)");
		System.out.println("2. 살쾡이 (레벨 5)");

		int choice = Integer.parseInt(in.nextLine());

		switch (choice) {
		case 1:
			monster_name = "너구리";
			monster_hp = 50;
			monster_defense = 5;
			monster_power = 20;
			monster_money = 10;
			monster_experience = 10;
			break;
		case 2:
			monster_name = "살쾡이";
			monster_hp = 2000;
			monster_level = 5;
			monster_power = 100;
			monster_defense = 20;
			monster_money = 30;
			monster_experience = 50;
			break;
		default:
			System.out.println("올바른 몬스터를 선택하세요.");
		}
	}

	static int hero_attack() {
		return hero_level * 10 + hero_power * 30;
	}

	static void hero_attacked(int sum) {
		if (sum <= monster_defense) {
			monster_hp = monster_hp;
		} else {
			monster_hp = monster_hp + monster_defense - sum;
		}
	}

	static int monster_attack() {
		return monster_power;
	}

	static void monster_attacked(int sum) {
		if (sum <= hero_defense) {
			hero_hp = hero_hp;
		} else {
			hero_hp = hero_hp + hero_defense - sum;
		}
	}

	static void levelUp() {
		hero_level++;
		int moneyIncrease = hero_level * 50; // 레벨업으로 돈이 얼마나 증가했는지 계산
		System.out.println("레벨이 올랐습니다!");
		hero_money += moneyIncrease;
		System.out.println("레벨업 기념으로 돈이 " + moneyIncrease + "원 증가했습니다.");
		System.out.println(hero_money + "원이 되었습니다.");
		hero_experience = 0;
	}

	static int potionStore_show(int money, int num) {
		hero_money -= money;

		switch (num) {
		case 1:
			hero_power += 3;
			break;
		case 2:
			hero_defense += 3;
			break;
		case 3:
			hero_experience += 50;
			break;
		case 4:
			hero_hp += 50;
			break;
		case 5:
			hero_mp += 50;
			break;
		default:
			System.out.println("올바른 번호를 입력하세요.");
		}

		System.out.println("구입이 완료되었습니다.");
		System.out.println(hero_money + "원 남았습니다.");
		return hero_money;
	}

	static void printHeroInfo() {
		System.out.println("---------------");
		System.out.println("Hero 정보");
		System.out.println("이름: " + hero_name);
		System.out.println("레벨: " + hero_level);
		System.out.println("힘: " + hero_power);
		System.out.println("방어력: " + hero_defense);
		System.out.println("HP: " + hero_hp);
		System.out.println("경험치: " + hero_experience);
		System.out.println("돈: " + hero_money);
		System.out.println("---------------");
	}

	static void mainBattleLoop(Scanner in) {
		while (true) {
			System.out.println(monster_name + "와 전투를 시작합니다.");

			while (hero_hp > 0 && monster_hp > 0) {
				System.out.println("히어로의 공격입니다.");

				int skillDamage = chooseSkill(in);

				System.out.println("데미지는 " + skillDamage + "입니다.");
				hero_attacked(skillDamage);

				if (monster_hp > 0) {
					int monsterDamage = monster_attack();
					System.out.println("몬스터의 공격입니다.");
					System.out.println("데미지는 " + monsterDamage + "입니다.");
					monster_attacked(monsterDamage);
				}
			}

			if (monster_hp <= 0) {
				System.out.println(monster_name + "를 물리쳤습니다!");
				hero_experience += monster_experience;
				hero_money += monster_money;
				System.out.println("경험치를 얻었습니다.");
				System.out.println("돈을 획득하였습니다.");
				if (hero_experience >= hero_level * 80) {
					levelUp();
				}
			}

			if (hero_hp <= 0) {
				System.out.println(hero_name + "이(가) 죽었습니다.");
				hero_hp = original_hero_hp;

				// 수정된 부분: enterHuntingGround()를 호출하는 부분을 수정
				System.out.print("계속해서 전투하시겠습니까? (Y/N): ");
				String continueBattle = in.nextLine();

				if (!continueBattle.equalsIgnoreCase("Y")) {
					break; // equals 문자열 비교 중에 대소문자 구분 안하는 함수
				} else {
					enterHuntingGround(); // 여기서 호출하도록 수정
					selectMonster(in);
				}
			}

			printHeroInfo();

			if (hero_hp > 0) {
				System.out.print("계속해서 전투하시겠습니까? (Y/N): ");
				String continueBattle = in.nextLine();

				if (!continueBattle.equalsIgnoreCase("Y")) {
					break;
				} else {
					hero_hp = original_hero_hp;
					monster_hp = 0;
					selectMonster(in);
				}
			}
		}
	}

	static int chooseSkill(Scanner in) {
		System.out.println("스킬을 선택하세요:");
		System.out.println("1. 쓰러스트 (데미지: " + (hero_level * 10 + hero_power * 30) + ")");

		int skillChoice = Integer.parseInt(in.nextLine());

		switch (skillChoice) {
		case 1:
			return hero_level * 10 + hero_power * 30;
		default:
			System.out.println("올바른 번호를 입력하세요.");
			return 0;
		}
	}

	static void potionStore(Scanner in) {
		while (true) {
			System.out.println("포션 상점에 입장하였습니다.");
			System.out.println("1. 힘 증강 포션(30원)");
			System.out.println("2. 방어력 증강 포션(30원)");
			System.out.println("3. 경험치 증강 포션(100원)");
			System.out.println("4. HP 증강 포션(10원)");
			System.out.println("5. MP 증강 포션(10원)");
			System.out.println("6. 나가기");

			System.out.print("원하시는 물건을 입력하세요 (1-6): ");
			int choice = Integer.parseInt(in.nextLine());

			if (choice == 6) {
				System.out.println("포션 상점에서 나갑니다.");
				break;
			}

			if (hero_money >= getItemPrice(choice)) {
				hero_money = potionStore_show(getItemPrice(choice), choice);
			} else {
				System.out.println("돈이 부족합니다.");
			}
		}
	}

	static int getItemPrice(int choice) {
		switch (choice) {
		case 1:
		case 2:
			return 30;
		case 3:
			return 100;
		case 4:
		case 5:
			return 10;
		default:
			return 0;
		}
	}
}
