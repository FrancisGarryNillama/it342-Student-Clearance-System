import React from "react";

const TaskItem = ({ task }) => {
  return (
    <div className="border p-4 rounded-md shadow-sm bg-white">
      <h3 className="text-lg font-semibold">Task #{task.taskId}</h3>
      <p className="text-sm text-gray-600">Status: <span className="font-medium">{task.status}</span></p>
      <p className="text-sm text-gray-500">Updated: {task.updatedAt || "N/A"}</p>
    </div>
  );
};

export default TaskItem;
