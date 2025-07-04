import React, { useEffect, useState } from "react";
import axios from "../../services/api";

const NotificationList = () => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    axios.get("/api/notifications").then((res) => {
      setNotifications(res.data);
    });
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-4">🔔 Notifications</h2>
      {notifications.length === 0 ? (
        <p className="text-gray-500">No notifications yet.</p>
      ) : (
        <ul className="space-y-3">
          {notifications.map((n) => (
            <li
              key={n.notificationId}
              className={`p-3 border rounded-md ${n.seen ? "bg-gray-100" : "bg-yellow-100"}`}
            >
              <p>{n.message}</p>
              <span className="text-xs text-gray-500">
                {new Date(n.createdAt).toLocaleString()}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default NotificationList;
