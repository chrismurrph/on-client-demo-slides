(ns intro.core
  (:require [jobim.core :as jobim
             :refer [slide-show default-style ->Title
                     ->CaptionedPic ->ClojureCode ->Picture
                     ->Code ->Text code]
             :refer-macros [defshow defclj pseudo-clj]]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [intro.components :as comps :refer [->ShowList]]))

(defshow om-next-client-side
         default-style
         (->Title
           "Om Next Client side" "Chris Murphy")
         (->CaptionedPic
           "/pictures/dog_food.jpg"
           "Om Next")
         (->ShowList ["component" "query" "query keyword" "default db format" "ident" "ident keyword"])
         (pseudo-clj 60
                     (defui AnyAction
                            Object
                            (render [this]
                                    (let [{:keys [text action]} (om/props this)]
                                      (dom/button #js{:onClick action} text))))
                     (def any-action (om/factory AnyAction))
                     (any-action {:text "Show State" :action #(pprint @my-reconciler)}))
         (pseudo-clj 80
                     [:id :line-name :selected?])
         (pseudo-clj 40
                     (defui Checkbox
                            static om/IQuery
                            (query [this]
                                   [:id :line-name :selected?])))
         (pseudo-clj 80
                     (defui Root
                            static om/IQuery
                            (query [this]
                                   [{:app/app-lines (om/get-query Checkbox)}
                                    {:graph/fake-graph (om/get-query FakeGraph)}
                                    {:graph/graph-lines (om/get-query Line)}
                                    ])))
         (->Text "Follow :graph/graph-lines query keyword to init-state and @my-reconciler")
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
