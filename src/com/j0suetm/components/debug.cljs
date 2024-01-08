(ns com.j0suetm.components.debug
  (:require
   [cljs.pprint :refer [pprint]]
   [helix.core :refer [defnc]]
   [helix.dom :as hd]
   [refx.alpha :as refx]))

(defnc debug []
  (let [db (refx/use-sub [:db])]
    (hd/pre (with-out-str (pprint db)))))
