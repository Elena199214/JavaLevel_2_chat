package Lesson_8;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

    public class App {

        public static void main(String[] args) {

            //terminal
            //collect
            //reduce
            //forEach
            //findFirst
            //anyMatch
            //allMatch


            //non-term
            //map
            //flatMap
            //filter
            //peek
            //skip
            //limit

            List<String> words = new ArrayList<>();
            words.add("zsdfasda");
            words.add("zsdfasda");
            words.add("DASDASD");
            words.add("DASDASD");
            words.add("asd");
            words.add("asda");
            words.add("a");
            words.add("a");
            words.add("a");
            words.add("ab");
            words.add("ab");
            words.add("ab");
            words.add("b");
            System.out.println(words);

            //1 Получить новый-кленовый список со словами длиннее 3х букв
            List<String> newList = words.stream().filter(w -> w.length() > 3).collect(Collectors.toList());
            System.out.println(newList);

            //2 Получить список длин слов
            List<Integer> intList = words.stream().map(String::length).distinct().sorted().collect(Collectors.toList());

            System.out.println(intList);

            //3 Map -> слово : длина
            System.out.println(words.stream()
                    .collect(Collectors.toMap(word -> word, w -> 1, Integer::sum))
                    .entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .peek(System.out::println)
                    .map(String::toUpperCase)
                    .collect(Collectors.joining(","))
            );


        }
    }

