package group60.perception;


public class TestClassify {
	public static void main(String args[]) {
		KnownObjectHandler koh = new KnownObjectHandler();
		for (KnownObject ko : koh.getKnownObjects()) {
			System.out.println(ko);
		}
	}
}
