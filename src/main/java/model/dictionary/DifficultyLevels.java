package model.dictionary;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */

public enum DifficultyLevels {

	EASY {
		@Override
		public String toString() {
			return "easy";
		}
	},

	MEDIUM {
		@Override
		public String toString() {
			return "medium";
		}
	},

	HARD {
		@Override
		public String toString() {
			return "hard";
		}
	};


	public static int getDifficultyId(String level) {
		if (level.equals(EASY.name())) return EASY.ordinal();
		else if (level.equals(MEDIUM.name())) return MEDIUM.ordinal();
		else if (level.equals(HARD.name())) return HARD.ordinal();
		else return Integer.parseInt(null);
	}

	public static String getDifficultyName(int id) {
		if (id == EASY.ordinal()) return EASY.name();
		else if (id == MEDIUM.ordinal()) return MEDIUM.name();
		else if (id == HARD.ordinal()) return HARD.name();
		else return null;
	}
}
