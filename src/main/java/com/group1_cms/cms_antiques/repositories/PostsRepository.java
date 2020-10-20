package com.group1_cms.cms_antiques.repositories;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.models.User;

@Repository
public class PostsRepository {
	private ArrayList<Post> posts;

	public PostsRepository() {
		User user1 = new User();
		User user2 = new User();
		User user3= new User();
		user1.setUserName("Bobloblaw");
		user2.setUserName("AntiquesMan");
		user3.setUserName("Crzy4Antiques");
		
		posts = new ArrayList<Post>();
		posts.add(new Post("Post Title", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 
				new Item("Doll", "Collectible"),
				user1));
		posts.add(new Post("Post Title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Convallis posuere morbi leo urna molestie at elementum eu. Aliquam faucibus purus in massa tempor nec feugiat nisl pretium. Facilisis magna etiam tempor orci eu lobortis elementum nibh. Neque laoreet suspendisse interdum consectetur. In fermentum et sollicitudin ac. Pellentesque sit amet porttitor eget dolor morbi. Et ultrices neque ornare aenean euismod elementum. Ut consequat semper viverra nam libero justo laoreet sit amet. Fermentum et sollicitudin ac orci phasellus egestas tellus rutrum. Ut tristique et egestas quis ipsum suspendisse ultrices gravida dictum. Volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. In hac habitasse platea dictumst vestibulum rhoncus est pellentesque elit. Ac placerat vestibulum lectus mauris ultrices. Pellentesque pulvinar pellentesque habitant morbi tristique senectus et. Viverra aliquet eget sit amet. Penatibus et magnis dis parturient. Integer malesuada nunc vel risus commodo. Aliquam eleifend mi in nulla posuere sollicitudin aliquam ultrices sagittis.\r\n" + 
				"\r\n" + 
				"Aliquet bibendum enim facilisis gravida neque. Tempor commodo ullamcorper a lacus vestibulum. Non enim praesent elementum facilisis leo vel fringilla. Pellentesque eu tincidunt tortor aliquam nulla facilisi cras fermentum odio. Morbi enim nunc faucibus a. Consectetur adipiscing elit duis tristique sollicitudin nibh sit amet. Pulvinar mattis nunc sed blandit. Lectus mauris ultrices eros in cursus turpis massa. Nec ultrices dui sapien eget mi proin. Auctor eu augue ut lectus arcu. In fermentum et sollicitudin ac orci phasellus. Neque viverra justo nec ultrices dui sapien eget mi proin. Sit amet tellus cras adipiscing enim eu. Leo vel fringilla est ullamcorper eget nulla facilisi etiam. Neque aliquam vestibulum morbi blandit cursus risus at ultrices mi. Tristique senectus et netus et malesuada fames ac turpis egestas.\r\n" + 
				"\r\n" + 
				"Mi proin sed libero enim sed faucibus turpis in eu. Tincidunt vitae semper quis lectus nulla at. Quam adipiscing vitae proin sagittis nisl. Nisl suscipit adipiscing bibendum est ultricies integer. Sagittis eu volutpat odio facilisis mauris sit amet massa. Egestas integer eget aliquet nibh praesent tristique magna. Mollis nunc sed id semper risus in hendrerit gravida rutrum. Sollicitudin ac orci phasellus egestas tellus rutrum tellus pellentesque. Enim sit amet venenatis urna cursus eget nunc. Aliquet porttitor lacus luctus accumsan. Eget dolor morbi non arcu risus quis varius quam. Fermentum iaculis eu non diam phasellus. Posuere ac ut consequat semper viverra. Sit amet consectetur adipiscing elit pellentesque habitant morbi tristique. Interdum varius sit amet mattis.\r\n" + 
				"\r\n" + 
				"Id volutpat lacus laoreet non curabitur gravida arcu ac tortor. Ullamcorper a lacus vestibulum sed arcu non odio euismod. Nulla malesuada pellentesque elit eget gravida cum sociis natoque. Eu nisl nunc mi ipsum faucibus vitae aliquet nec. Lacinia at quis risus sed. Cras sed felis eget velit aliquet sagittis id consectetur purus. Feugiat in fermentum posuere urna nec. Aliquet nibh praesent tristique magna sit amet purus gravida quis. Adipiscing at in tellus integer feugiat. Nibh praesent tristique magna sit amet purus gravida quis blandit. Ullamcorper malesuada proin libero nunc consequat interdum varius. Purus viverra accumsan in nisl nisi scelerisque eu ultrices. Nisl vel pretium lectus quam id leo in.\r\n" + 
				"\r\n" + 
				"Risus sed vulputate odio ut enim blandit volutpat. Odio pellentesque diam volutpat commodo sed egestas egestas fringilla phasellus. Ut etiam sit amet nisl. Vulputate odio ut enim blandit volutpat maecenas. Habitant morbi tristique senectus et netus et malesuada fames ac. Pellentesque nec nam aliquam sem. Dignissim cras tincidunt lobortis feugiat. Dolor sit amet consectetur adipiscing elit duis. Lacus sed turpis tincidunt id aliquet risus feugiat. Imperdiet dui accumsan sit amet.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"© 2015 — 2020",
				new Item("Chair", "Furniture"),
				user2));
		posts.add(new Post("Post Title", "ut consequat semper viverra nam libero justo laoreet sit amet cursus sit amet dictum sit amet justo donec enim diam vulputate ut pharetra sit amet aliquam id diam maecenas ultricies mi eget mauris pharetra et ultrices neque ornare aenean euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna neque viverra justo nec ultrices dui sapien eget mi proin sed libero enim sed faucibus turpis in eu mi bibendum neque egestas congue quisque egestas diam in arcu cursus euismod quis viverra nibh cras pulvinar mattis nunc sed blandit libero volutpat sed cras ornare arcu dui", new Item("Couch", "Furniture"),user1));
		posts.add(new Post("Post Title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. At volutpat diam ut venenatis tellus in metus vulputate. Et leo duis ut diam quam nulla. Purus faucibus ornare suspendisse sed nisi lacus. Neque sodales ut etiam sit amet nisl purus in. Id donec ultrices tincidunt arcu. Elementum sagittis vitae et leo duis. Hac habitasse platea dictumst quisque sagittis purus sit amet volutpat. Scelerisque eleifend donec pretium vulputate. Ut sem viverra aliquet eget. Augue eget arcu dictum varius duis at consectetur. Urna nunc id cursus metus aliquam eleifend mi in. Aliquet nibh praesent tristique magna sit amet purus gravida. Consequat nisl vel pretium lectus quam id leo in. Dui sapien eget mi proin. Donec massa sapien faucibus et. Tellus pellentesque eu tincidunt tortor aliquam nulla facilisi. At elementum eu facilisis sed odio morbi.\r\n" + 
				"\r\n" + 
				"Leo vel orci porta non. Senectus et netus et malesuada fames. Magna etiam tempor orci eu lobortis. Ac turpis egestas maecenas pharetra convallis posuere. Eu sem integer vitae justo eget magna fermentum. Sem fringilla ut morbi tincidunt augue interdum velit. Massa tincidunt dui ut ornare lectus. Suspendisse sed nisi lacus sed viverra tellus in hac habitasse. Mus mauris vitae ultricies leo integer malesuada. Amet dictum sit amet justo donec enim. Magnis dis parturient montes nascetur ridiculus mus mauris. Sit amet justo donec enim diam vulputate ut pharetra.\r\n" + 
				"\r\n" + 
				"Justo laoreet sit amet cursus sit amet dictum sit. Faucibus purus in massa tempor nec. Cras tincidunt lobortis feugiat vivamus at augue eget. Id diam maecenas ultricies mi eget mauris. Velit egestas dui id ornare arcu odio ut sem nulla. Et ultrices neque ornare aenean euismod elementum nisi. In massa tempor nec feugiat nisl pretium fusce id. Libero nunc consequat interdum varius sit amet mattis vulputate. Ultrices gravida dictum fusce ut. Leo vel orci porta non pulvinar neque laoreet suspendisse interdum. Cras semper auctor neque vitae tempus quam pellentesque. Diam sollicitudin tempor id eu. In arcu cursus euismod quis viverra nibh. Ultrices sagittis orci a scelerisque purus semper eget duis at. Purus faucibus ornare suspendisse sed nisi lacus sed viverra tellus. Volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque. Mauris a diam maecenas sed. Elementum eu facilisis sed odio morbi quis commodo odio aenean. Odio facilisis mauris sit amet massa. Ut venenatis tellus in metus vulputate.\r\n" + 
				"\r\n" + 
				"Et netus et malesuada fames ac turpis. Amet consectetur adipiscing elit ut. Id donec ultrices tincidunt arcu non sodales neque sodales ut. Adipiscing diam donec adipiscing tristique. Leo in vitae turpis massa sed elementum tempus egestas. Massa tempor nec feugiat nisl. Dignissim cras tincidunt lobortis feugiat vivamus at augue eget arcu. Quam elementum pulvinar etiam non quam lacus suspendisse faucibus. Ut diam quam nulla porttitor massa id neque. Placerat vestibulum lectus mauris ultrices eros. Interdum velit laoreet id donec. Dis parturient montes nascetur ridiculus mus mauris vitae ultricies. Eget sit amet tellus cras adipiscing.\r\n" + 
				"\r\n" + 
				"Odio aenean sed adipiscing diam donec adipiscing. Id leo in vitae turpis massa sed elementum. Tincidunt ornare massa eget egestas purus. Proin sed libero enim sed faucibus. Tristique et egestas quis ipsum suspendisse ultrices gravida dictum. Quisque id diam vel quam elementum pulvinar etiam non quam. Vitae tempus quam pellentesque nec nam aliquam sem et tortor. Sagittis aliquam malesuada bibendum arcu. Odio morbi quis commodo odio aenean sed adipiscing. Sociis natoque penatibus et magnis dis parturient montes. Etiam non quam lacus suspendisse. In mollis nunc sed id. Lacus laoreet non curabitur gravida. Pellentesque diam volutpat commodo sed. Pharetra convallis posuere morbi leo urna molestie at elementum.\r\n" + 
				"\r\n" + 
				"Quam viverra orci sagittis eu. Risus nec feugiat in fermentum posuere urna nec tincidunt praesent. Netus et malesuada fames ac turpis. Massa enim nec dui nunc mattis enim. Nunc sed id semper risus in hendrerit gravida. Nec feugiat in fermentum posuere urna nec tincidunt praesent. Tortor at risus viverra adipiscing at in. Scelerisque purus semper eget duis at. Duis ultricies lacus sed turpis tincidunt id aliquet. Amet cursus sit amet dictum sit amet. Eu mi bibendum neque egestas. Tortor posuere ac ut consequat semper viverra nam libero. Amet tellus cras adipiscing enim. Pellentesque habitant morbi tristique senectus. Tristique magna sit amet purus gravida quis blandit turpis. Interdum velit euismod in pellentesque massa. Sit amet dictum sit amet justo donec enim diam. Proin sagittis nisl rhoncus mattis.\r\n" + 
				"\r\n" + 
				"Convallis aenean et tortor at risus viverra adipiscing at. In iaculis nunc sed augue lacus viverra. Mi ipsum faucibus vitae aliquet nec. Aliquet lectus proin nibh nisl. Dictum fusce ut placerat orci nulla. Eget nulla facilisi etiam dignissim. Quisque sagittis purus sit amet volutpat consequat mauris nunc congue. Nibh praesent tristique magna sit amet purus gravida quis. Porta non pulvinar neque laoreet suspendisse interdum consectetur libero. Non arcu risus quis varius quam quisque. Blandit turpis cursus in hac habitasse platea dictumst quisque.\r\n" + 
				"\r\n" + 
				"Tempus iaculis urna id volutpat lacus laoreet non curabitur. Dui nunc mattis enim ut tellus. Non enim praesent elementum facilisis leo vel fringilla. Placerat in egestas erat imperdiet sed euismod nisi. Ante in nibh mauris cursus mattis molestie a. Lorem donec massa sapien faucibus. Velit dignissim sodales ut eu. Nisl tincidunt eget nullam non nisi est. Ut morbi tincidunt augue interdum velit euismod in pellentesque. Aliquet bibendum enim facilisis gravida neque convallis a cras semper. Vulputate sapien nec sagittis aliquam malesuada bibendum. Lobortis mattis aliquam faucibus purus in massa. Potenti nullam ac tortor vitae purus. Suspendisse sed nisi lacus sed viverra tellus. Tincidunt arcu non sodales neque sodales ut etiam sit amet. Laoreet non curabitur gravida arcu ac tortor dignissim convallis. Nulla facilisi cras fermentum odio eu feugiat pretium. Orci eu lobortis elementum nibh.\r\n" + 
				"\r\n" + 
				"Aliquam faucibus purus in massa tempor nec feugiat nisl. Velit dignissim sodales ut eu sem integer vitae justo. At in tellus integer feugiat scelerisque. Non consectetur a erat nam at lectus urna duis convallis. Scelerisque viverra mauris in aliquam sem fringilla ut. Rutrum quisque non tellus orci ac auctor augue mauris. Urna condimentum mattis pellentesque id nibh. Ultrices vitae auctor eu augue ut. Commodo viverra maecenas accumsan lacus vel. Eros donec ac odio tempor orci dapibus ultrices. Odio tempor orci dapibus ultrices in iaculis nunc sed augue. Fermentum leo vel orci porta non pulvinar neque laoreet suspendisse. Magna ac placerat vestibulum lectus mauris.\r\n" + 
				"\r\n" + 
				"Risus viverra adipiscing at in tellus integer feugiat. Aliquet lectus proin nibh nisl. Ut morbi tincidunt augue interdum velit euismod in pellentesque. Mattis aliquam faucibus purus in massa tempor nec feugiat nisl. Condimentum lacinia quis vel eros donec ac odio tempor. Sed viverra ipsum nunc aliquet bibendum enim. Vulputate odio ut enim blandit volutpat maecenas volutpat. Amet tellus cras adipiscing enim eu turpis egestas pretium aenean. Quis varius quam quisque id diam. Tincidunt dui ut ornare lectus sit amet est. Mauris rhoncus aenean vel elit scelerisque.", 
				new Item("Painting", "Art"),
				user3));
		posts.add(new Post("Post Title", "", new Item("Ring", "Jewelry"), user1));
		posts.add(new Post("Post Title", "", new Item("Necklace", "Jewelry"), user2));
		posts.add(new Post("Post Title", "", new Item("Toy Car", "Collectible"),user3));
		posts.add(new Post("Post Title", "", new Item("Dress", "Clothing"),user1));
		posts.add(new Post("Post Title", "", new Item("Pants", "Clothing"),user2));
		posts.add(new Post("Post Title", "", new Item("Hat", "Clothing"),user3));
		posts.add(new Post("Post Title", "", new Item("Shirt", "Clothing"),user1));
		posts.add(new Post("Post Title", "", new Item("Table", "Furniture"),user2));
		posts.add(new Post("Post Title", "", new Item("Stool", "Furniture"),user3));
		posts.add(new Post("Post Title", "", new Item("Pottery", "Art"),user1));
		posts.add(new Post("Post Title", "", new Item("Socks", "Clothing"),user2));
		posts.add(new Post("Post Title", "", new Item("Crown", "Jewelry"),user3));
		posts.add(new Post("Post Title", "", new Item("Painting", "Art"),user1));
		posts.add(new Post("Post Title", "", new Item("Stand", "Furniture"),user2));
		posts.add(new Post("Post Title", "", new Item("Bed", "Furniture"),user3));
		posts.add(new Post("Post Title", "", new Item("Car", "Collectible"),user1));
		posts.add(new Post("Post Title", "", new Item("Painting 1", "Art"),user2));
		posts.add(new Post("Post Title", "", new Item("Painting 2", "Art"),user3));
		posts.add(new Post("Post Title", "", new Item("Painting 3", "Art"),user1));
		posts.add(new Post("Post Title", "", new Item("Painting 4", "Art"),user2));
		posts.add(new Post("Post Title", "", new Item("Painting 5", "Art"),user3));
		posts.add(new Post("Post Title", "", new Item("Painting 6", "Art"),user1));
		posts.add(new Post("Post Title", "", new Item("Painting 7", "Art"),user2));
		posts.add(new Post("Post Title", "", new Item("Painting 8", "Art"),user3));
		posts.add(new Post("Post Title", "", new Item("Painting 9", "Art"),user1));
		posts.add(new Post("Post Title", "", new Item("Painting 10", "Art"),user2));	
	}
	
	public ArrayList<Post> getPosts(int numberOfPosts){
		ArrayList<Post> posts = new ArrayList<Post>();
		this.posts.stream()
		.limit(10)
		.forEachOrdered(posts::add);
		return posts;
	}
	
public ArrayList<Post> getPostsFromCategory(int numberOfPosts, String category){
		
		ArrayList<Post> posts = new ArrayList<Post>();
		this.posts.stream()
		.filter(post -> post.getItem().getCategory().equalsIgnoreCase(category))
		.limit(10)
		.forEachOrdered(posts::add);
		
		return posts;
	}
}
