package gameClient;

import api.game_service;
import Server.Game_Server_Ex2;
public class Ex2 {
	public static void main(String[] args) {
		int level_number=0;
		game_service game=Game_Server_Ex2.getServer(level_number);
	}

}
