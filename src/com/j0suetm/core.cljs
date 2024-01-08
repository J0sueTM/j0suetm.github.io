(ns com.j0suetm.core
  (:require
   ["react" :as react]
   ["react-dom/client" :as react-dom-client]
   [com.j0suetm.db]
   [com.j0suetm.events]
   [com.j0suetm.views :as views]
   [helix.core :refer [$]]
   [refx.alpha :as refx]))

(enable-console-print!)

(defonce app-root
  (-> js/document
      (.getElementById "app")
      react-dom-client/createRoot))

(defn render []
  (->> ($ views/home)
       ($ react/StrictMode)
       (.render app-root)))

(defn ^:dev/after-load clear-cache-and-render! []
  (refx/clear-subscription-cache!)
  (render))

(defn ^:export init []
  (refx/dispatch [:initialize-db])
  (render))
