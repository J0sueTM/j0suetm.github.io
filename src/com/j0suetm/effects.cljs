(ns com.j0suetm.effects
  (:require
   [refx.alpha :as refx]))

(defn apply-request! [{:keys [uri on-resp]}]
  ;; TODO
  )

(defn http-fx [req-fn]
  (fn [req]
    (if (sequential? req)
      (for [r req]
        (apply-request! r))
      (apply-request! req))))

(refx/reg-fx
 :http
 http-fx)
