(ns slides.core
  (:require [jobim.core :as jobim
             :refer [slide-show default-style ->Title
                     ->CaptionedPic ->ClojureCode ->Picture
                     ->Code ->Text code]
             :refer-macros [defshow defclj pseudo-clj pseudo-clj-points]]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [slides.components :as comps :refer [->Points]]))

(defshow advent
         default-style
         (->Title
           "Advent of Code" "Chris Murphy")
         (->Points ["read-string"
                    "re-find, re-matches, re-seq"
                    "partition, partition-all, partition-by"
                    "iterate"
                    "reductions"
                    "take-while, drop-while"
                    "Canonical"])
         (pseudo-clj-points 73
                            ["R2, L1, R2, R1, R1, L3, R3, L5,"]
                            (def data (->> (str "[" (slurp (io/resource "one.txt")) "]")
                                           read-string
                                           (map name))))
         (pseudo-clj-points 41
                            ["\"bot 75 gives low to bot 145 and high to bot 95\""
                             "([\"bot 75\" \"bot\" \"75\"] [\"bot 145\" \"bot\" \"145\"] [\"bot 95\" \"bot\" \"95\"])"
                             "(:bot75 :bot145 :bot95)"
                             ""
                             "\"bot 96 gives low to output 10 and high to bot 66\""
                             "(:bot96 :output10 :bot66)"]
                            (defn addr-names [l]
                              (->> (re-seq #"(bot|output)\s(\d+)" l)
                                   (map (comp
                                          keyword
                                          #(apply str %)
                                          rest)))))
         (pseudo-clj-points 70
                            ["\"12abc12\""
                             "((1 2) (a b c) (1 2))"
                             "((1 2) (1 2))"
                             "(12 12)"
                             "24"]
                            (defn add-numbers [in]
                              (->> in
                                   (partition-by digit-or-minus?)
                                   (filter #(-> % first digit-or-minus?))
                                   (map coll->whole-number)
                                   (reduce +))))
         (pseudo-clj 40
                     (defn add-numbers [in]
                       (->> in
                            (re-seq "-?\\d+")
                            (map read-string)
                            (reduce +))))
         (pseudo-clj 80
                     (->> (iterate transition-state (make-init start-state))
                          (drop-while (complement finished?))
                          first
                          extract-res
                          ))
         (pseudo-clj 50
                     (def start-state [0 [1 2 3]])
                     (def make-init identity)
                     (defn transition-state [[counted triple]]
                       [(inc counted) (map inc triple)])
                     (defn finished? [[_ triple]]
                       (zero? (mod (reduce * triple) 910)))
                     (defn extract-res [[counted _]] counted))
         (pseudo-clj-points 60
                            ["({:bot74 #{67}, :bot110 #{29}, :bot3 #{5} ...} { ... } ...)"]
                            (->> (iterate transition-state (make-init (map parse-line data)))
                                 (take-while #(not (nil? %)))
                                 (map :registers)
                                 (map #(some (fn [[k v]] (when (= #{61 17} v) k)) %))
                                 (filter #(not (nil? %)))
                                 first))
         (->Points
           ["Follow the provided sequence: either turn left (L) or right (R) 90 degrees"
            "Then walk forward the given number of blocks"
            "Easter Bunny HQ is actually at the first location you visit twice."])
         (pseudo-clj-points 60
                            ["-1"]
                            (({"L" dec "R" inc} "L") 0))
         (pseudo-clj-points 70
                            ["2"]
                            (reduce (fn [acc ele] (({"L" dec "R" inc} ele) acc)) 0 ["L" "R" "R" "R"]))
         (pseudo-clj-points 60
                            ["(5 6)"]
                            (map + (list 5 5) (list 0 1)))
         (pseudo-clj-points 50
                            ["(2 0)"]
                            (reduce (partial map +) (list 0 0) [(list 1 0) (list 1 0)]))
         (pseudo-clj-points 60
                            ["[\"R\" 23], [\"L\" 1], [\"R\" 2], [\"R\" 10], [\"R\" 34], [\"R\" 30]"
                             "(1 0 1 2 3 4)"
                             "(1 0 1 2 3 0)"
                             "([1 0] [0 1] [1 0] [0 -1] [-1 0] [0 1])"
                             "([1 0] [1 0] ..."
                             "((0 0) (1 0) (2 0) ...)"]
                            (defn positions [d]
                              (->> (map first d)
                                   (reductions #(({\L dec \R inc} %2) %1) 0)
                                   rest
                                   (map #(mod % 4))
                                   (map [[0 1] [1 0] [0 -1] [-1 0]])
                                   (mapcat repeat (map second d))
                                   (reductions (partial map +) (list 0 0)))))
         (pseudo-clj 30
                     (defn point-to-dist [p]
                       (->> p
                            (map #(Math/abs %))
                            (reduce +))))
         (pseudo-clj 40
                     (conj (list) 0)
                     (0)
                     (conj '(0) 1)
                     (1 0)
                     (conj '(1 0) 2)
                     (2 1 0))
         (pseudo-clj-points 40
                            ["((0 0) (1 0) (2 0) (3 0) (4 0) (4 -1) (4 -2) (4 -3) (3 -3) (2 -3) (2 -2) (2 -1) (2 0) (2 1))"
                             "(() ((0 0)) ((1 0) (0 0)) ((2 0) (1 0) (0 0)) ...)"
                             "(((2 0) (2 -1) (2 -2) (2 -3) (3 -3) (4 -3) (4 -2) (4 -1) (4 0) (3 0) (2 0) (1 0) (0 0)))"
                             "(2 0)"
                             "2"]
                            (->> [[\R 4] [\R 3] [\R 2] [\R 4]]
                                 positions
                                 (reductions conj (list))
                                 (filter (fn [[x & xs]] ((set xs) x)))
                                 ffirst
                                 point-to-dist
                                 ))
         )
