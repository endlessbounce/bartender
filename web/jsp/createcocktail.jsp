<div class="row">
    <div class="col">
        <h1>App</h1>

        <div ng-controller="viewController as ctrl">

            <div>

                <p>Add:</p>
                <input type="text" ng-model="ctrl.newTodo"></div>
            <button ng-click="ctrl.addNewTodo()">Add</button>

            <div>
                <p>List:</p>
                <button ng-hide="ctrl.editMode" ng-click="ctrl.triggerEditMode()">Edit</button>
                <button ng-show="ctrl.editMode" ng-click="ctrl.triggerEditMode()">Done</button>
                <ol>
                    <li ng-repeat="todo in ctrl.todos track by $index">
                        <span ng-hide="ctrl.editMode" ng-bind="todo"></span>
                        <input ng-model="todo" ng-show="ctrl.editMode" type="text"></input>
                        <button ng-hide="ctrl.editMode" ng-click="ctrl.deleteTodo($index)">Delete</button>
                    </li>
                </ol>
            </div>

        </div>
    </div>
</div>