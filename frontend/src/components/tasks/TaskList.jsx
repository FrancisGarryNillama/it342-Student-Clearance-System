import React from "react";
import TaskItem from "./TaskItem";

const TaskList = ({ tasks }) => {
  if (tasks.length === 0) return <p className="text-gray-500">No tasks found.</p>;

  return (
    <div className="space-y-4 mt-4">
      {tasks.map((task) => (
        <TaskItem key={task.taskId} task={task} />
      ))}
    </div>
  );
};

export default TaskList;
