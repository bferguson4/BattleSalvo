package cs3500.pa04.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SalvoDatabase {

  private Path hitsFile;
  private List<Coord> previousHits;

  public SalvoDatabase() {
    this.hitsFile = Path.of("src/main/resources/HitData.sr");
    previousHits = getOrderedHits();
  }

  public Coord getShot(List<Coord> availableShots) {
    if (previousHits.size() > 0) {
      Coord shot = previousHits.remove(0);
      while (previousHits.size() > 0 && !availableShots.contains(shot)) {
        shot = previousHits.remove(0);
      }
      if (!availableShots.contains(shot)) {
        return randomShot(availableShots);
      }
      availableShots.remove(shot);
      return shot;
    }
    return randomShot(availableShots);
  }

  private Coord randomShot(List<Coord> availableShots) {
    Random random = new Random();
    int randIndex = random.nextInt(availableShots.size());
    return availableShots.remove(randIndex);
  }


  public void writeHits(List<Coord> hits) {
    Scanner scan = getScanner();
    StringBuilder hitsString = new StringBuilder();
    for (Coord hit : hits) {
      hitsString.append(hit).append("\n");
    }
    while (scan.hasNext()) {
      hitsString.append(scan.nextLine()).append("\n");
    }

    String contents = hitsString.toString();
    byte[] data = contents.getBytes();

    try {
      Files.write(hitsFile, data);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Scanner getScanner() {
    try {
      return new Scanner(hitsFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Coord> getOrderedHits() {
    Scanner scan = getScanner();
    List<Coord> hits = new ArrayList<>();
    while (scan.hasNext()) {
      Coord coord = Coord.stringToCoord(scan.nextLine());
      hits.add(coord);
    }
    HashMap<Coord, Integer> hitsMap = new HashMap<>();
    for (int i = 0; i < hits.size(); i++) {
      Coord coord = hits.get(i);
      int count = 1;
      if (!hitsMap.containsKey(coord)) {
        for (int k = 0; k < hits.size(); k++) {
          if (k != i && coord.equals(hits.get(k))) {
            count++;
          }
        }
        hitsMap.put(coord, count);
      }
    }
    List<Coord> orderedHits = new ArrayList<>();
    for (Coord hit : hitsMap.keySet()) {
      if (orderedHits.size() == 0) {
        orderedHits.add(hit);
      } else {
        int index = 0;
        int hitValue = hitsMap.get(hit);
        while (index < orderedHits.size() && hitsMap.get(orderedHits.get(index)) > hitValue) {
          index++;
        }
        orderedHits.add(index, hit);
      }
    }
    return orderedHits;
  }
}
