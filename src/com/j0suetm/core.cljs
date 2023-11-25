(ns com.j0suetm.core
  (:require
   ["react" :as react]
   ["react-dom/client" :as react-dom-client]
   [helix.core :refer [$ defnc]]
   [helix.dom :as hd]
   [refx.alpha :as refx]))

(defnc home-view []
  (hd/div {:class "bg-dark0"}
          (hd/img {:src "resources/assets/hero.png"})))

(defonce app-root
  (react-dom-client/createRoot (.getElementById js/document "app")))

(defn render []
  (.render app-root ($ react/StrictMode ($ home-view))))

(defn ^:dev/after-load clear-cache-and-render! []
  (refx/clear-subscription-cache!)
  (render))

(defn ^:export init []
  (render))
