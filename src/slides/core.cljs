(ns intro.core
  (:require [jobim.core :as jobim
             :refer [slide-show default-style ->Title
                     ->CaptionedPic ->ClojureCode ->Picture
                     ->Code ->Text code]
             :refer-macros [defshow defclj pseudo-clj]]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [intro.components :as comps :refer [->ShowList]]))

(defclj code-slide 40
  (def a (+ 1 2))
  (def b (+ a 3))
  (defn c [d] (+ d b a))
  (defn d [e]
    (if (= e :do-it)
      (c 10)
      (c 15)))
  (d :do-it))

(defshow om-next-client-side
         default-style
         (->Title
           "Om Next Client side" "Chris Murphy")
         (->CaptionedPic
           "/pictures/dog_food.jpg"
           "Om Next")
         (->ShowList ["component" "query" "query keyword" "default db format" "ident" "ident keyword"])
         (pseudo-clj 80
                     (defshow show-name
                              slide-style
                              (->Title "title" "subtitle")
                              (->Text "text")
                              (->Picture "url")
                              (->CaptionedPic "url" "text")))
         (->Text "Most of our technical slide shows are just plain strings or files!")
         (->Text "What if we could write techincal presentations in actual testable code!")
         code-slide
         (pseudo-clj 80
                     (defclj code-slide 40
                             (def a (+ 1 2))
                             (def b (+ a 3))
                             (defn c [d] (+ d b a))
                             (defn d [e]
                               (if (= e :do-it)
                                 (c 10)
                                 (c 15)))
                             (d :do-it)))
         (pseudo-clj 80
                     (deftest code-slide-test
                       (is (= (get (:env show/code-slide) :a) 3))
                       (is (= (get (:env show/code-slide) :b) 6))
                       (is (= ((get (:env show/code-slide) :c) 1) 10))
                       (is (= (get (:env show/code-slide) 4) 19))))
         (->Text "Tests run live in a figwheel environment while you develop.")
         (->Text "The Slide Abstraction: How to Extend Jobim")
         (pseudo-clj 80
                     (defprotocol Slide
                       (render-slide [this])
                       (next-slide [this state])
                       (prev-slide [this state])))
         (->Text "What about other languages?")
         (code
           "javascript"
           "function test(){"
           ["console.log(\"This is a JS function\");"]
           "};")
         (pseudo-clj 40
                     (code
                       "javascript"
                       "function test(){"
                       ["console.log(\"This is a JS function\");"]
                       "};"))
         (code
           "python"
           "def test():"
           ["print \"Jobim can do Python too!\""])
         (pseudo-clj 40
                     (code
                       "python"
                       "def test():"
                       ["print \"Jobim can do Python too!\""])))
