(ns clj-json-patch.core
  (:require [clj-json-patch.util :as util :refer [apply-patch diff* *keywordize*]]))


(defn call-apply-patch
  [obj patches]
  (reduce #(util/apply-patch %1 %2) obj patches))



(defn patch
  "Applies a JSON patch document (multiple patches) to JSON object."
  ([obj patches]
   (call-apply-patch obj patches))
  ([obj patches keywordize?]
   (binding [util/*keywordize* keywordize?]
     (call-apply-patch obj patches))))

(defn call-diff*
  [obj1 obj2 prefix]
  #?(:clj   (util/diff* obj1
                   obj2
                   "/")
     :cljs  (util/diff* (js->clj (.parse js/JSON obj1))
                        (js->clj (.parse js/JSON obj2))
                        "/")))

(defn diff
  "Prepares a JSON patch document representing the difference
  between two JSON objects."
  ([obj1 obj2]
   (call-diff* obj1 obj2 "/"))
  ([obj1 obj2 keywordize?]
   (binding [util/*keywordize* keywordize?]
    (call-diff* obj1 obj2 "/"))))
