package main.java.nl.tue.ieis.get.event.publisher;

public class StartAllPublisher {

	public static void main(String[] args) {
		new Thread () {
		public void run () {
			String[] args = {};
			Source1849Publisher.main(args);
		  }
		}.start();
		new Thread () {
			public void run () {
				String[] args = {};
				Source5246Publisher.main(args);
			  }
			}.start();
	new Thread () {
		public void run () {
			String[] args = {};
			Source7256Publisher.main(args);
		  }
		}.start();
	new Thread () {
		public void run () {
			String[] args = {};
			OtherJdRPublisher.main(args);
		  }
		}.start();
	}
}
