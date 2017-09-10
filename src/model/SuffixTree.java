package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class SuffixTree {
	private int oo = 100000000;
	private List<CustomToken> sequence;
	private List<Node> nodes;
	private int root;
	private int currentNode;
	private int currentLabel;
	private int currentSize;
	private int remainder = 0;
	
	public SuffixTree(Collection<List<CustomToken>> systemTokens) {
		sequence = buildSequence(systemTokens);
		nodes = new ArrayList<>();
		root = currentNode = addNode(0, 0, 0);
		buildTree();
	}
	
	/**
	 * Construit la superséquence de tokens
	 * @param tokens
	 * @return
	 */
	private List<CustomToken> buildSequence(Collection<List<CustomToken>> tokens) {
		List<CustomToken> superSequence = new ArrayList<>();  
	    AtomicInteger last = new AtomicInteger(1000);

		tokens.forEach(list -> {
			list.add(new CustomToken("", 0, last.intValue(), "customFirst"));
			superSequence.addAll(list);
			last.incrementAndGet();
		});
		
		System.out.println(superSequence.size());
		return superSequence;
	}
	
	/**
	 * Construit l'arbre des suffixes
	 */
	private void buildTree() {
		for(int i = 0; i < sequence.size(); i++) {
			treatment(i);
		}
	}
	
	/**
	 * Effectie le traitement pour la phase i
	 * @param i
	 */
	private void treatment(int i) {
		int suffixLink = -1;
		remainder++;
		
		while(remainder > 0) {
			if(currentSize == 0) {
				currentLabel = i;
			}
			
			if(!nodes.get(currentNode).getSons().containsKey(sequence.get(currentLabel))) {
				suffixLink = addLeave(i, suffixLink);
			} else {
				int son = nodes.get(currentNode).getSons().get(sequence.get(currentLabel));
				
				if(walkDown(i, son)) {
					continue;
				}			
				
				if(sequence.get(nodes.get(son).getBegin() + currentSize).equals(sequence.get(i))) {
					currentSize++;
					majSuffixLink(suffixLink, currentNode);
					break;
				}
				
				suffixLink = split(i, son, suffixLink);
			}
			
			remainder--;
			
			maj(i);
		}
	}
	
	/**
	 * Ajoute une feuille
	 * @param i
	 * @param suffixLink
	 * @return
	 */
	private int addLeave(int i, int suffixLink) {
		int leave = addNode(i, oo, nodes.get(currentNode).getSize() + sequence.size() - i); 
		nodes.get(leave).addSet(i - remainder + 1);
		nodes.get(currentNode).getSons().put(sequence.get(currentLabel), leave);
		
		return majSuffixLink(suffixLink, currentNode);
	}
	
	/**
	 * Sépare un noeud interne en deux
	 * @param i
	 * @param son
	 * @param suffixLink
	 * @return
	 */
	private int split(int i, int son, int suffixLink) {
		int split = addNode(nodes.get(son).getBegin(), nodes.get(son).getBegin() + currentSize, nodes.get(currentNode).getSize() + currentSize);
		nodes.get(split).setSet(new ArrayList<>(nodes.get(son).getSet()));
		nodes.get(split).addSet(i - remainder + 1);
		nodes.get(currentNode).getSons().put(sequence.get(currentLabel), split);
		int leave = addNode(i, oo, nodes.get(split).getSize() + sequence.size() - i);
		nodes.get(leave).addSet(i - remainder + 1);
		nodes.get(split).getSons().put(sequence.get(i), leave);
		nodes.get(son).setBegin(nodes.get(son).getBegin() + currentSize);
		nodes.get(split).getSons().put(sequence.get(nodes.get(son).getBegin()), son);
		
		return majSuffixLink(suffixLink, split);
	}
	
	private boolean walkDown(int i, int son) {
		int size = Math.min(nodes.get(son).getEnd(), i + 1) - nodes.get(son).getBegin();
		
		if(currentSize >= size) {
			nodes.get(son).addSet(i - remainder + 1);
			nodes.get(nodes.get(son).getSuffixLink()).addSet(i - remainder + 2);
			currentLabel += size;
			currentSize -= size;
			currentNode = son;
			return true;
		}	
		
		return false;
	}
	
	private void maj(int i) {
		if(currentNode == root && currentSize > 0) {
			currentSize--;
			currentLabel = i - remainder + 1;
		} else {
			currentNode = nodes.get(currentNode).getSuffixLink() > 0 ? nodes.get(currentNode).getSuffixLink() : root;
		}
	}
	
	private int addNode(int begin, int end, int size) {
		Node node = new Node(begin, end, size);
		nodes.add(node);
		return nodes.indexOf(node);
	}
	
	private int majSuffixLink(int suffixLink, int node) {
		if(suffixLink > 0) {
			nodes.get(suffixLink).setSuffixLink(node);
		}
		
		return node;
	}
	
	/**
	 * Compare tous les fichiers d'un projet avec l'arbre des suffixes
	 * @param map
	 * @param threshold
	 * @return une liste de clones
	 */
	public List<Clone> search(Map<File, List<CustomToken>> map, int threshold) {
	    List<Clone> clones = new ArrayList<>();
		
	    map.entrySet().stream().forEach(entry -> { 
			if(entry.getValue().size() >= threshold)
				clones.addAll(searchFile(entry, threshold));
		});
	    
	    map.clear();
				
		return clones;
	}
	
	private List<Clone> searchFile(Entry<File, List<CustomToken>> entry, int threshold) {
		List<CustomToken> motif = entry.getValue();
		List<Clone> clones = new ArrayList<>();
		boolean isContained = false;

		Point p = pathMatching(new Point(root, 0), motif.subList(0, motif.size()));
		List<Clone> found = reportMatches(motif, p, threshold, new ArrayList<>(), isContained);
		
		if(!found.isEmpty())
			clones.addAll(found);
		
		for(int i = 1; i < motif.size() - threshold + 1; i++) {
			Point n;

			if(p.getNode() != root && nodes.get(p.getNode()).getSuffixLink() != root) {
				int l = nodes.get(p.getNode()).getSuffixLink();
				n = next(new Point(l, p.getSpan()), motif.subList(i, motif.size()));
				isContained = true;
			} else {
				if(p.getSpan() > 1) {
					n = next(new Point(root, p.getSpan() - 1), motif.subList(i, motif.size()));
					isContained = true;
				} else {
					n = pathMatching(new Point(root, 0), motif.subList(i, motif.size()));
					isContained = false;
				}
			}
			
			found = reportMatches(motif.subList(i, motif.size()), n, threshold, clones.isEmpty() ? new ArrayList<>() : clones.get(clones.size() - 1).getTokens(), isContained);
			
			if(!found.isEmpty())
				clones.addAll(found);
			
			p = n;
		}
				
		 return clones;
	}
	
	private Point pathMatching(Point p, List<CustomToken> motif) {	
		boolean mismatch = false;
		Node node = nodes.get(p.getNode()); 
		int size = node.getSize(); 
		int span = p.getSpan(); 
		int i = size + span;
		
		while(i < motif.size() && node.getSons().containsKey(motif.get(size)) && !mismatch) {
			Node son = nodes.get(node.getSons().get(motif.get(size)));
			
			if(son.getSize() - node.getSize() == span) {
				node = son;
				size = node.getSize();
				
				if(node.getSons().containsKey(motif.get(size))) {
					span = 1;
				} else {
					mismatch = true;
					span = 0;
				}
			} else {
				if(!sequence.get(son.getBegin() + span).equals(motif.get(i))) {
					mismatch = true;
				} else {
					span++;
				}
			}
			
			i++;
		}
		
		return new Point(nodes.indexOf(node), span);
	}
	
	private Point next(Point p, List<CustomToken> motif) {	
		Node node = nodes.get(p.getNode());
		int i = node.getSize();
		int limit = p.getSpan() + i;

		while(i < motif.size() && node.getSons().containsKey(motif.get(i)) && nodes.get(node.getSons().get(motif.get(i))).getSize() <= limit) {
			node = nodes.get(node.getSons().get(motif.get(i)));
			i = node.getSize();
		}
		
		return pathMatching(new Point(nodes.indexOf(node), limit - node.getSize()), motif);
	}
	
	private List<Clone> reportMatches(List<CustomToken> motif, Point p, int threshold, List<CustomToken> last, boolean isContained) { 
		List<Clone> clones = new ArrayList<>();
		
		if(nodes.get(p.getNode()).getSize() + p.getSpan() >= threshold) {
			List<CustomToken> found = motif.subList(0, nodes.get(p.getNode()).getSize() + p.getSpan());

			if(last.isEmpty()) {
				clones.addAll(check(motif, found, p));
			} else {
				int begin = last.size() - found.size() > 0 ? last.size() - found.size() : 0;
				if(!isContained || !found.equals(last.subList(begin, last.size()))) {
					clones.addAll(check(motif, found, p));
				}
			}
		}
		
		return clones;
	}
	
	private List<Clone> check(List<CustomToken> motif, List<CustomToken> found, Point p) {
		List<Clone> clones = new ArrayList<>();
		Node node = nodes.get(p.getNode());
		
		if(p.getSpan() > 0) {
			node = nodes.get(node.getSons().get(motif.get(node.getSize())));
		}
		
		for(int i = 0; i < node.getSet().size(); i++) {
			List<CustomToken> match = sequence.subList(node.getSet().get(i), node.getSet().get(i) + nodes.get(p.getNode()).getSize() + p.getSpan());
			clones.add(new Clone(match.get(0).getName(), match.get(0).getLine(), match.get(match.size() - 1).getLine(), found.get(0).getName(), found.get(0).getLine(), found.get(found.size() - 1).getLine(), match));
		}
				
		return clones;
	}
	
	public int getSize() {
		return sequence.size();
	}
	
	public int getNumberNode() {
		return nodes.size();
	}

}